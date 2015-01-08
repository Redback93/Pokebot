package twitchpokedex.twitch.listeners;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ModeEvent;
import org.pircbotx.hooks.events.OpEvent;
import org.pircbotx.hooks.events.UserModeEvent;

public class GenericListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onEvent(Event<PircBotX> event) throws Exception 
	{
		super.onEvent(event);
		System.out.println(event.toString());
	}
	
	@Override
	public void onMode(ModeEvent<PircBotX> event) throws Exception
	{
		super.onMode(event);
		
		if(event.getMode().startsWith("+o"))
		{
			for(User channelUser : event.getChannel().getUsers())
			{
				
			}
		}
		else if(event.getMode().startsWith("-o"))
		{
			
		}
		System.out.println(event.getMode());
	}
}
