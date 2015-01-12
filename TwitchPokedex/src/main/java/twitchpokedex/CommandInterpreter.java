package twitchpokedex;

import java.util.List;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import twitchpokedex.constants.Constants;
import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.PartyPokemon;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.User;
import twitchpokedex.pokeballs.Pokeball;
import twitchpokedex.pokedollars.DollarUpdater;
import twitchpokedex.twitch.TwitchAPI;
import twitchpokedex.twitch.api.TwitchUser;
import twitchpokedex.utils.Localisation;
import twitchpokedex.utils.PokePicker;

@RequiredArgsConstructor
public class CommandInterpreter
{
	@NonNull private org.pircbotx.User chatUser;
	@NonNull private User tpUser;
	@NonNull private MessageEvent<PircBotX> ircEvent;
	
	public void interpret(String commandTerm, List<String> args)
	{
		//Allow for non-users
		if(commandTerm.equalsIgnoreCase("capture"))
			capturePokemon();
		else if(tpUser.isDefault())
		{
			ircEvent.respond(Localisation.getString("errors.NotJoined"));
			return;
		}
		
		DollarUpdater.AddUser(tpUser);
		
		if(commandTerm.equalsIgnoreCase("levelup"))
			levelupPokemon(args);
		else if(commandTerm.equalsIgnoreCase("points"))
			getPoints();
	}
	
	/**
	 * Runs the capture pokemon algorithm and adds the new pokemon to the party 
	 */
	private void capturePokemon()
	{
		List<PartyPokemon> party = DBConn.GetUserParty(tpUser);
		//Create the new user on the first capture event
		if(tpUser.isDefault()) createNewUser();
		
		//Check for party full
		if(party.size() >= Constants.MAX_PARTY_POKEMON)
		{
			ircEvent.respond(Localisation.getString("errors.PartyFull"));
			return;
		}
		
		boolean isUnique = true;
		Pokemon newPokemon;
		do
		{
			newPokemon = PokePicker.GetRandomPokemon(Pokeball.Normalball);
			
			//Check pokemon doesn't exist
			for(PartyPokemon mon : party)
				if(mon.getPokemon().getId() == newPokemon.getId())
					isUnique=false;
		}
		while(!isUnique);
		
		int slot = 0;
		//Check for empty party slot
		if(party.size() != 0)
			//Last filled party slot
			slot = party.get(party.size()-1).getSlot() + 1;
		
		PartyPokemon newPP = new PartyPokemon(); //NewPartyPokemon
		newPP.setLevel(1); //Default to level 1
		newPP.setName(null); //Default to no name
		newPP.setPokemon(newPokemon); //Set to the algorithm pokemon
		newPP.setSlot(slot);
		newPP.setUser(tpUser.getId()); //Set to tpUser
		DBConn.SaveOrUpdateObject(newPP);
		
		ircEvent.respond(Localisation.getString("actions.PokemonCaptured", newPokemon.getName()));
	}
	
	/**
	 * Levels up the pokemon specified by the user
	 */
	private void levelupPokemon(List<String> args)
	{
		//Check for appropriate arguments
		if(args.isEmpty())
		{
			ircEvent.respond(Localisation.getFormat("format.Levelup"));
			return;
		}
		
		String pokemonName = args.get(0);
		List<PartyPokemon> party = DBConn.GetUserParty(tpUser);
		PartyPokemon selectedPokemon = null;
		
		//Scroll through all party pokemon to find theirs
		for(PartyPokemon poke : party)
		{
			String partyPokeName = poke.getPokemon().getName();
			
			if(partyPokeName.equalsIgnoreCase(pokemonName))
			{
				selectedPokemon = poke;
				break;
			}
		}
		
		//Respond to user if not found
		if(selectedPokemon == null)
		{
			ircEvent.respond(Localisation.getString("errors.PokemonNotFound"));
			return;
		}
		
		int levelupCost = DBConn.GetCost("Levelup");
		//Ensure enough pokedollars to pay for operation
		if(tpUser.getPoints() < levelupCost)
		{
			ircEvent.respond(Localisation.getString("errors.NotEnoughDollars", levelupCost));
			return;
		}
		
		//Pays for the service
		tpUser.removePoints(levelupCost);
		selectedPokemon.levelUp();
		tpUser.update();
		selectedPokemon.update();
		
		ircEvent.respond(Localisation.getString("actions.PokemonLevelled", selectedPokemon.getDisplayName(), selectedPokemon.getLevel()));
	}

	/**
	 * Outputs the users points
	 */
	private void getPoints()
	{
		ircEvent.respond(Localisation.getString("info.CurrentPoints", tpUser.getPoints()));
	}
	
	/**
	 * Creates a new user
	 */
	private void createNewUser()
	{
		TwitchUser twitchUser = TwitchAPI.GetTwitchUser(chatUser.getNick());
		User newUser = User.Create(twitchUser.getId(), twitchUser.getName(), twitchUser.getDisplayName());
		newUser.save();
		
		tpUser = DBConn.GetUser(twitchUser.getName());
	}
}
