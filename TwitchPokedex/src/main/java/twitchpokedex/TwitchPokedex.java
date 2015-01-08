package twitchpokedex;

import twitchpokedex.database.DBConn;
import twitchpokedex.twitch.TwitchChat;

/**
 * The main running class for the bot
 * @author Redback
 *
 */
public class TwitchPokedex 
{
	public static void main(String[] args)
	{
		DBConn.Connect();
		
		TwitchChat chat = new TwitchChat(new String[]{"Redback93"});
		chat.Start();
	}
}
