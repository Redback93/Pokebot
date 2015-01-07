package twitchpokedex.twitch.handlers;

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
    }
}
