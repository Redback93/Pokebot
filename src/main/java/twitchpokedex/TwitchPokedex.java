package twitchpokedex;

import twitchpokedex.database.DatabaseConnection;
import twitchpokedex.twitch.TwitchChat;

public class TwitchPokedex 
{
	public static void main(String[] args)
	{
		TwitchChat chat = new TwitchChat();
		try {
			chat.Start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DatabaseConnection db = new DatabaseConnection();
		db.GetUsers();
	}
}
