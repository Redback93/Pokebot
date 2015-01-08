package twitchpokedex.twitch.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class ModeratorListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onMessage(MessageEvent event) throws Exception
	{
		if(!event.getUser().isIrcop())
			return;
		
		if(event.getMessage().equalsIgnoreCase("!hello"))
			event.respond("Hello there!");
		
		if(event.getMessage().toLowerCase().startsWith("!setpokemon"))
		{
			String[] splitString = event.getMessage().split(" ");
			
		    if (splitString.length < 3)
            {
                event.respond("Must be in format: !setpokemon -username- -pokemon-");
                return;
            }
            s
            tring targetPokemon = splitString[2];
		    
		    
		    
		    
		}
    }
}
