package twitchpokedex.twitch.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TradeListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		if (event.getMessage().equalsIgnoreCase("!trade"))
			event.respond("Trade initiated");
	}
}
