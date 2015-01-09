package twitchpokedex.twitch.listeners;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import twitchpokedex.constants.Constants;
import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.PartyPokemon;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.User;
import twitchpokedex.pokeballs.Pokeball;
import twitchpokedex.utils.Localisation;
import twitchpokedex.utils.PokePicker;

public class ChannelListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		String[] messageParts = StringUtils.split(event.getMessage(), " ");
		char leadingChar = messageParts[0].charAt(0);
		
		event.getUser().setDonator(true);
		
		if(leadingChar != '!') return;
		
		User targetUser = DBConn.GetUserByUsername(event.getUser().getNick());
		String commandTerm = messageParts[0].substring(1);
		
		if(commandTerm.equalsIgnoreCase("capture"))
			CapturePokemon(event, targetUser);
	}
	
	/**
	 * Runs the capture pokemon algorithm and adds the new pokemon to the party
	 * @param event
	 * @param target The user to add the new pokemon to 
	 */
	private void CapturePokemon(MessageEvent<PircBotX> event, User target)
	{
		List<Pokemon> allPokemon = DBConn.GetAllPokemon();
		Pokemon newPokemon = PokePicker.GetRandomPokemon(Pokeball.UltraBall);
		
		List<PartyPokemon> party = DBConn.GetUserParty(target);
		
		//Check for party full
		if(party.size() >= Constants.MAX_PARTY_POKEMON)
		{
			event.respond(Localisation.getString("errors.PartyFull"));
			return;
		}
		
		int slot = 0;
		//Check for empty party slot
		if(party.size() != 0)
			//Last filled party slot
			slot = party.get(party.size()-1).getSlot() + 1;
		
		PartyPokemon newPP = new PartyPokemon(); //NewPartyPokemon
		newPP.setLevel(1); //Default to level 1
		newPP.setName(null); //Default to no name
		newPP.setPokemon(newPokemon.getId()); //Set to the algorithm pokemon
		newPP.setSlot(slot);
		newPP.setUser(target.getId()); //Set to target user
		DBConn.NewPartyPokemon(newPP);
		
		event.respond(Localisation.getString("actions.PokemonCaptured", newPokemon.getName()));
	}
}
