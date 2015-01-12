package twitchpokedex.twitch.listeners;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import twitchpokedex.CommandInterpreter;
import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.User;

public class ChannelListener extends ListenerAdapter<PircBotX>
{
	/**
	 * Handle all of the messages in a typical channel
	 */
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		List<String> messageParts = Arrays.asList(StringUtils.split(event.getMessage(), " "));
		char leadingChar = messageParts.get(0).charAt(0);
		
		if(leadingChar != '!') return;
		
		//Grab the user from db (or non-user)
		User targetUser = DBConn.GetUser(event.getUser().getNick());
		event.getUser().setDonator(targetUser.hasDonated());
		
		(new Thread(){
			public void run() {
				CommandInterpreter interpreter = new CommandInterpreter(event.getUser(), targetUser, event);
				String commandTerm = messageParts.get(0).substring(1);
				List<String> args = messageParts.subList(1, messageParts.size());
				
				interpreter.interpret(commandTerm, args);
			}
		}).start();	
	}
}