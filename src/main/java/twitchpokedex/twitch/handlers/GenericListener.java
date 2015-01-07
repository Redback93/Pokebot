package twitchpokedex.twitch.handlers;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.UserModeEvent;

public class GenericListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onEvent(Event event) throws Exception 
	{
		System.out.println(event.toString());
	}
	
	@Override
	public void onOp(OpEvent event) throws Exception
	{
		super.onOp(event);
		
		System.out.println(event.getRecipient());
	}
}
