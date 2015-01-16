package twitchpokedex;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import twitchpokedex.constants.Constants;
import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.PartyPokemon;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.User;
import twitchpokedex.items.Item;
import twitchpokedex.items.ItemManager;
import twitchpokedex.items.Pokeball;
import twitchpokedex.items.Usable;
import twitchpokedex.pokedollars.DollarUpdater;
import twitchpokedex.twitch.TwitchAPI;
import twitchpokedex.twitch.api.TwitchUser;
import twitchpokedex.utils.Localisation;
import twitchpokedex.utils.PokePicker;

@RequiredArgsConstructor
public class CommandInterpreter
{
	@NonNull
	private org.pircbotx.User chatUser;
	@NonNull
	private User tpUser;
	@NonNull
	private MessageEvent<PircBotX> ircEvent;

	public void interpret(String commandTerm, List<String> args)
	{
		// Allow for non-users
		if (commandTerm.equalsIgnoreCase("capture"))
			capturePokemon(args);
		else if (tpUser.isDefault()) {
			respond(Localisation.getString("errors.NotJoined"));
			return;
		}

		DollarUpdater.AddUser(tpUser);

		if (commandTerm.equalsIgnoreCase("levelup"))
			levelupPokemon(args);
		else if (commandTerm.equalsIgnoreCase("points"))
			getPoints();
		else if (commandTerm.equalsIgnoreCase("use"))
			useItem(args);
		else if(commandTerm.equalsIgnoreCase("party"))
			getParty();
		else if(commandTerm.equalsIgnoreCase("bag"))
			getBag();
	}

	/**
	 * Runs the capture pokemon algorithm and adds the new pokemon to the party
	 * 
	 * @param args
	 */
	private boolean capturePokemon(List<String> args)
	{
		List<PartyPokemon> party = tpUser.getParty();
		// Create the new user on the first capture event
		if (tpUser.isDefault())
			createNewUser();

		// Check for party full
		if (party.size() >= Constants.MAX_PARTY_POKEMON)
			return respond(Localisation.getString("errors.PartyFull"));

		// Determine pokéball to use
		Pokeball pokeball;

		// Default to normal ball
		if (args.size() < 1)
			pokeball = Pokeball.normalball;
		else {
			// Do a safe cast to an item first
			Item specifiedItem = ItemManager.getItem(args.get(0));
			// Do a safe cast to pokeball, if possible
			pokeball = (specifiedItem instanceof Pokeball) ? (Pokeball) specifiedItem
					: null;
			if (pokeball == null)
				return respond(Localisation.getString(
						"errors.UnrecognisedItem", args.get(0)));
		}

		// TODO: Take pokeball out of inventory

		boolean isUnique = true;
		Pokemon newPokemon;
		do {
			newPokemon = PokePicker.GetRandomPokemon(pokeball);

			// Check pokemon doesn't exist
			for (PartyPokemon mon : party)
				if (mon.getPokemon().getId() == newPokemon.getId())
					isUnique = false;
		} while (!isUnique);

		int slot = 0;
		// Check for empty party slot
		if (party.size() != 0)
			// Last filled party slot
			slot = party.get(party.size() - 1).getSlot() + 1;

		PartyPokemon newPP = new PartyPokemon(); // NewPartyPokemon
		newPP.setLevel(1); // Default to level 1
		newPP.setName(null); // Default to no name
		newPP.setPokemon(newPokemon); // Set to the algorithm pokemon
		newPP.setSlot(slot);
		newPP.setUser(tpUser);
		party.add(newPP);
		DBConn.SaveOrUpdateObject(newPP);

		return respond(Localisation.getString("actions.PokemonCaptured",
				newPP));
	}

	/**
	 * Levels up the pokemon specified by the user
	 */
	private boolean levelupPokemon(List<String> args)
	{
		// Check for appropriate arguments
		if (args.isEmpty())
			return respond(Localisation.getFormat("format.Levelup"));

		String pokemonName = args.get(0);
		PartyPokemon selectedPokemon = getPartyPokemon(pokemonName);

		// Respond to user if not found
		if (selectedPokemon == null)
			return respond(Localisation.getString("errors.PokemonNotFound"));

		int levelupCost = DBConn.GetCost("Levelup");
		// Ensure enough pokedollars to pay for operation
		if (tpUser.getPoints() < levelupCost)
			return respond(Localisation.getString("errors.NotEnoughDollars",
					levelupCost));

		// Pays for the service
		tpUser.removePoints(levelupCost);
		selectedPokemon.levelUp();
		tpUser.update();
		selectedPokemon.update();

		return respond(Localisation.getString("actions.PokemonLevelled",
				selectedPokemon, selectedPokemon.getLevel()));
	}

	/**
	 * Outputs the user's points
	 */
	private boolean getPoints()
	{
		return respond(Localisation.getString("info.CurrentPoints",
				tpUser.getPoints()));
	}
	
	/**
	 * Outputs the user's party
	 */
	private boolean getParty()
	{
		String responseString = StringUtils.join(tpUser.getParty(), ", ");
		return respond(Localisation.getString("info.CurrentParty", responseString));
	}

	/**
	 * Uses an item in the player's inventory
	 * @param args The arguments for the command
	 */
	private boolean useItem(List<String> args)
	{
		if(args.size() < 1) respond(Localisation.getFormat("format.Use"));
		
		int offset = 0;
		Item retrievedItem;
		do
		{
			//Could not find item
			if(offset+1 >= args.size())
				return respond(Localisation.getString("errors.ItemNotFound"));
			//Item name is everything except target
			String itemName = StringUtils.join(args.subList(0, args.size()-offset++), " ");
			retrievedItem = ItemManager.getItem(itemName);
		}
		while(!(retrievedItem instanceof Usable));
		
		Usable usableItem = (Usable) retrievedItem;
		
		//Does it require a target which wasn't passed
		if(usableItem.isTargetRequired() && offset < 2)
			return respond(Localisation.getString("errors.RequiresTarget"));
		
		String target = (args.size()<2) ? null : args.get(args.size()-1);
		
		if(usableItem == Usable.shiny_stone)
		{
			PartyPokemon pokemon = getPartyPokemon(target);
			if(pokemon == null)
				return respond(Localisation.getString("errors.PokemonNotFound"));
			if(pokemon.isShiny())
				return respond(Localisation.getString("errors.PokemonIsAlready", "shiny"));
			pokemon.setShiny(true);
			pokemon.update();
			return respond(Localisation.getString("actions.PokemonShiny", pokemon.getPokemon().getName()));
		}
		else if(usableItem == Usable.mega_stone)
		{
			PartyPokemon pokemon = getPartyPokemon(target);
			if(pokemon == null)
				return respond(Localisation.getString("errors.PokemonNotFound"));
			if(pokemon.isMega())
				return respond(Localisation.getString("errors.PokemonIsAlready", "mega"));
			pokemon.setMega(true);
			pokemon.update();
			return respond(Localisation.getString("actions.PokemonMega", pokemon.getPokemon().getName()));
		}
		
		return false;
	}
	
	/**
	 * Outputs the user's bag
	 */
	private boolean getBag()
	{
		return true;
	}

	/**
	 * Creates a new user
	 */
	private boolean createNewUser()
	{
		TwitchUser twitchUser = TwitchAPI.GetTwitchUser(chatUser.getNick());
		User newUser = User.create(twitchUser.getId(), twitchUser.getName(),
				twitchUser.getDisplayName());
		newUser.save();

		tpUser = DBConn.GetUser(twitchUser.getName());
		return true;
	}

	/**
	 * Sends a message to respond to the user
	 * @param message The message to respond to the user
	 */
	private boolean respond(String message)
	{
		ircEvent.respond(message);
		return true;
	}
	
	/**
	 * Gets a particular pokemon in a user's party
	 * @param name The name of the pokemon to get
	 * @return The party pokemon or null if not found
	 */
	private PartyPokemon getPartyPokemon(String name)
	{
		PartyPokemon selectedPokemon = null;

		// Scroll through all party pokemon to find theirs
		for (PartyPokemon poke : tpUser.getParty()) {
			String partyPokeName = poke.getPokemon().getName();

			if (partyPokeName.equalsIgnoreCase(name)) {
				selectedPokemon = poke;
				break;
			}
		}
		
		return selectedPokemon;
	}
}
