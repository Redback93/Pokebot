using System;

namespace Pokébot
{
	//Wrapper class for the IRC bot
	//Reads configuration and starts bot
	class Launcher
	{
		//Entry point for all C# code
		public static void Main(string[] args)
		{
			//Load the IRC settings
			string username = "";
            string password = "";
			string channel = "";


			//Create a new instance of the IRC bot
			IRCBot newBot = new IRCBot(username, password, channel);
			newBot.Run();
		}
	}
}
