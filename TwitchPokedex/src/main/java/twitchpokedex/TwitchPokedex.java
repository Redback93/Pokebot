package twitchpokedex;

import twitchpokedex.database.DBConn;
import twitchpokedex.pokedollars.DollarUpdater;
import twitchpokedex.twitch.TwitchChat;
import twitchpokedex.utils.Localisation;

/**
 * The main running class for the bot
 * @author Redback
 *
 */
public class TwitchPokedex 
{
	public static void main(String[] args)
	{
		Localisation.SetLocale("en");
		DBConn.Connect();
		
		DollarUpdater.StartUpdating();
		
		TwitchChat chat = new TwitchChat(new String[]{"Redback93"});
		chat.Start();
	}
}
