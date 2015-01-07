using System;
using System.Collections.Generic;
using System.Dynamic;
using System.IO;
using System.Linq;
using System.Text;
using System.Timers;
using Newtonsoft.Json;
using Sharkbite.Irc;

namespace Pokébot
{
    //Controls all IRC behaviours
    //Implements the ThresherIRC library
    public class IRCBot
    {
        //Declare IRC connection
        private Connection connection;

        //Declares the requested channel
        private string channel;

        //A list of players in the bot
        private Dictionary<string, Player> players = new Dictionary<string, Player>();

        //A list of trades initiated by a user
        private Dictionary<Player, Player> trades = new Dictionary<Player, Player>();

        //The timer which tracks points
        private Timer pointsTimer;

        //The list of usernames of the moderators
        private List<string> moderators = new List<string>();

        //The list of pokemon and their assosciated IDs
        private Dictionary<int, string> pokemon = new Dictionary<int, string>();

        /*
         * Method that handles all messages in the channel
         * user: An object containing information about the user that spoke
         * channel: The channel that the message was spoken to
         * message: The message that the user sent
         */
        void OnPublic(UserInfo user, string channel, string message)
        {
            Console.WriteLine("Received Message: " + user.User + ":" + message);

            if (!players.ContainsKey(user.User.ToLower()))
                players.Add(user.User.ToLower(), new Player(user.User));

            Player currentPlayer = players[user.User.ToLower()];

            //Check for pokéinfo command
            if (message.ToLower() == "!pokeinfo")
            {
                //Send pokéinfo to channel
                SendMessage("PokéPoints are gained automatically every 10 seconds just for being in the chat! For the full list of features type !commands");
            }
            else if (message.ToLower() == "!battle")
            {
                Random rand = new Random();
                if (rand.NextDouble() < 0.5)
                    SendMessage("You won.");
                else SendMessage("You lost.");
            }
            //Check for !commands command
            else if (message.ToLower() == "!commands")
            {
                //Send !commands to channel
                SendMessage("!pokeinfo !lookup !points !party !capture !release !trade !levelup");
            }  
            //Check for party command
            else if (message.ToLower() == "!party")
            {
                if (currentPlayer.Pokemon != 0)
                    SendMessage(currentPlayer.Username + " owns a level " + currentPlayer.Level + " " + pokemon[currentPlayer.Pokemon]);
                else
                    SendMessage(currentPlayer.Username + " doesn't own a pokemon.");
            }
            //Checks for !levelup command
            else if (message.ToLower() == "!levelup")
            {
                if (currentPlayer.Points < 150)
                {
                    SendMessage("You need 150 points to level up. (" + currentPlayer.Points + " currently)");
                    return;
                }
                if (currentPlayer.Level == 100)
                {
                    SendMessage("Your " + pokemon[currentPlayer.Pokemon] + " is already level 100.");
                    return;
                }
                if (currentPlayer.Pokemon == 0)
                {
                    SendMessage(currentPlayer.Username + " doesn't own a pokemon.");
                    return;
                }
                currentPlayer.Points -= 150;
                currentPlayer.Level += 1;
                SendMessage(currentPlayer.Username + " has levelled their " + pokemon[currentPlayer.Pokemon] + " to level " + currentPlayer.Level);
                SaveSettings();
            }
            //Check for !maxlevel command
            else if(message.ToLower() == "!maxlevel")
            {
                if (currentPlayer.Points < 150)
                {
                    SendMessage("You need 150 points to level up. (" + currentPlayer.Points + " currently)");
                    return;
                }
                if (currentPlayer.Level == 100)
                {
                    SendMessage("Your " + pokemon[currentPlayer.Pokemon] + " is already level 100.");
                    return;
                }
                if (currentPlayer.Pokemon == 0)
                {
                    SendMessage(currentPlayer.Username + " doesn't own a pokemon.");
                    return;
                }
                var levels = (currentPlayer.Points/150);
                var leftover = (currentPlayer.Points%150);

                if (levels + currentPlayer.Level > 100)
                {
                    levels = 100 - currentPlayer.Level;
                    leftover = currentPlayer.Points - (levels*150);
                }

                currentPlayer.Level += levels;
                currentPlayer.Points = leftover;
                SendMessage(currentPlayer.Username + " has levelled their " + pokemon[currentPlayer.Pokemon] + " to level " + currentPlayer.Level);
                SaveSettings();
            }
            //Check for timer on command
            else if (message.ToLower() == "!timeron" && moderators.Contains(user.User.ToLower()))
            {
                pointsTimer.Start();
                SendMessage("The timer is now running.");
                SaveSettings();
            }
            //Check for timer off command
            else if (message.ToLower() == "!timeroff" && moderators.Contains(user.User.ToLower()))
            {
                pointsTimer.Stop();
                SendMessage("The timer is no longer running.");
                SaveSettings();
            }
            //Checks for play lookup commannd
            else if (message.ToLower().StartsWith("!lookup"))
            {
                //Split the message up into two strings by the space
                //0th string is !lookup
                //1st string is the username that they give
                string[] splitString = message.Split(' ');

                //If there aren't two strings, stop send back error
                if (splitString.Length < 2)
                {
                    //Send error message
                    SendMessage("Must be in format: !lookup -user-");
                    //Stop processing message
                    return;
                }

                string target = splitString[1].ToLower();
                if (!players.ContainsKey(target))
                {
                    //Send error message
                    SendMessage("That person doesn't exist.");
                    //Stop processing message
                    return;
                }
                Player targetPlayer = players[target];
                //Send back user points if they are being tracked
                if (targetPlayer.Level > 0)
                    SendMessage(target + " has a level " + targetPlayer.Level + " " + pokemon[targetPlayer.Pokemon]);
                else
                    SendMessage(target + " does not have a pokemon.");
            }

                //Checks for !points commannd
            else if (message.ToLower().StartsWith("!points"))
            {
                SendMessage(currentPlayer.Username + " points: " + currentPlayer.Points);
            }
            //Checks for set pokemon command
            else if (message.ToLower().StartsWith("!setpokemon") && moderators.Contains(user.User.ToLower()))
            {
                //Split the message up into three strings by the space
                //0th string is !setpokemon
                //1st string is the username that is given
                //2nd string is the pokemon to set to
                string[] splitString = message.Split(' ');

                if (splitString.Length < 3)
                {
                    SendMessage("Must be in format: !setpokemon -username- -pokemon-");
                    return;
                }

                string target = splitString[1].ToLower();
                if (!players.ContainsKey(target))
                {
                    //Send error message
                    SendMessage("That person doesn't exist.");
                    //Stop processing message
                    return;
                }
                Player targetPlayer = players[target];
                string targetPokemon = splitString[2];

                int pokemonID = 0;
                foreach (KeyValuePair<int, string> pair in pokemon)
                {
                    if (pair.Value.ToLower() == targetPokemon.ToLower())
                        pokemonID = pair.Key;
                }

                if (pokemonID == 0)
                {
                    SendMessage(targetPokemon + " is not a Pokémon.");
                    return;
                }

                targetPlayer.Pokemon = pokemonID;
                SendMessage(targetPlayer.Username + " now has a " + pokemon[pokemonID]);
            }
            //Checks for add point command
            else if (message.ToLower().StartsWith("!addpoints") && moderators.Contains(user.User.ToLower()))
            {
                //Split the message up into three strings by the space
                //0th string is !addpoints
                //1st string is the username that they give
                //2nd string is the number of points to send
                string[] splitString = message.Split(' ');


                //If there aren't two strings, stop send back error
                if (splitString.Length < 3)
                {
                    //Send error message
                    SendMessage("Must be in format: !addpoints -username- -points-");
                    //Stop processing message
                    return;
                }

                //Put array elements into their own variables

                string target = splitString[1].ToLower();
                if (!players.ContainsKey(target))
                {
                    //Send error message
                    SendMessage("That person doesn't exist.");
                    //Stop processing message
                    return;
                }
                Player targetPlayer = players[target];

                int pointAmount = 0;
                if (!int.TryParse(splitString[2], out pointAmount))
                {
                    //Send error message
                    SendMessage("Must be in format: !addpoints -username- -points-");
                    //Stop processing message
                    return;
                }

                //Check that target user exists
                targetPlayer.Points += pointAmount;
                SendMessage(currentPlayer.Username + " has given " + targetPlayer.Username + " " + pointAmount + " points.");
                SaveSettings();
            }
            //Checks for remove point command
            else if (message.ToLower().StartsWith("!removepoints") && moderators.Contains(user.User.ToLower()))
            {
                //Split the message up into three strings by the space
                //0th string is !removepoints
                //1st string is the username that they give
                //2nd string is the number of points to take away
                string[] splitString = message.Split(' ');

                //If there aren't two strings, stop send back error
                if (splitString.Length < 3)
                {
                    //Send error message
                    SendMessage("Must be in format: !removepoints -username- -points-");
                    //Stop processing message
                    return;
                }

                //Put array elements into their own variables
                string target = splitString[1].ToLower();
                if (!players.ContainsKey(target))
                {
                    //Send error message
                    SendMessage("That person doesn't exist.");
                    //Stop processing message
                    return;
                }
                Player targetPlayer = players[target];

                int pointAmount = 0;
                if (!int.TryParse(splitString[2], out pointAmount))
                {
                    //Send error message
                    SendMessage("Must be in format: !addpoints -username- -points-");
                    //Stop processing message
                    return;
                }

                //Check that target user exists
                targetPlayer.Points = (targetPlayer.Points < pointAmount ? 0 : targetPlayer.Points - pointAmount);
                SendMessage(currentPlayer.Username + " has removed " + targetPlayer.Username + " " + pointAmount + " points.");
                SaveSettings();
            }
            //Checks for the capture command
            else if (message.ToLower() == "!capture")
            {
                if (currentPlayer.Pokemon != 0)
                {
                    SendMessage("You already have a pokemon in your party.");
                    return;
                }

                //Choose a random number of one of the pokemon
                Random rand = new Random((int)(DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond));
                var newPoke = rand.Next(pokemon.Count);

                currentPlayer.Pokemon = newPoke;
                currentPlayer.Level = 1;

                SendMessage(currentPlayer.Username + " has captured a level 1 " + pokemon[newPoke]);
                SaveSettings();
            }
            //Checks for the release command
            else if (message.ToLower() == "!release")
            {
                //Does the user have a pokemon already
                if (currentPlayer.Pokemon == 0)
                {
                    SendMessage("You don't have a pokemon in your party.");
                    return;
                }

                //Does the user have enough points
                if (currentPlayer.Points < 150)
                {
                    SendMessage("You need 150 PokéPoints to release. (" + currentPlayer.Points + " currently)");
                    return;
                }

                currentPlayer.Points -= 150;
                var oldPoke = currentPlayer.Pokemon;
                currentPlayer.Pokemon = 0;
                currentPlayer.Level = 0;
                SendMessage(currentPlayer.Username + " has released their " + pokemon[oldPoke]);
                SaveSettings();
            }
            //Checks for the trade command
            else if (message.ToLower().StartsWith("!trade"))
            {
                //Split the message up into three strings by the space
                //0th string is !trade
                //1st string is the username that they give
                string[] splitString = message.Split(' ');

                //If there aren't two strings, stop send back error
                if (splitString.Length < 2)
                {
                    //Send error message
                    SendMessage("Must be in format: !trade -username-");
                    //Stop processing message
                    return;
                }

                //Put array elements into their own variables
                string target = splitString[1].ToLower();
                if (!players.ContainsKey(target))
                {
                    //Send error message
                    SendMessage("That person doesn't exist.");
                    //Stop processing message
                    return;
                }
                Player targetPlayer = players[target];

                //They're already trading with this user
                if (trades.ContainsKey(currentPlayer) && trades[currentPlayer] == targetPlayer)
                {
                    SendMessage("You are already trading with " + targetPlayer.Username);
                    return;
                }

                //Check if they're accepting a trade request
                if (trades.ContainsKey(targetPlayer) && trades[targetPlayer] == currentPlayer)
                {
                    int targetPokemon = targetPlayer.Pokemon;
                    int userPokemon = currentPlayer.Pokemon;

                    if (targetPokemon == 0 || userPokemon == 0)
                    {
                        SendMessage("Both users must have pokemon in their party.");
                        return;
                    }

                    //The actual swapping
                    targetPlayer.Pokemon = userPokemon;
                    currentPlayer.Pokemon = targetPokemon;

                    int userLevel = currentPlayer.Level;
                    int targetLevel = targetPlayer.Level;

                    targetPlayer.Level = userLevel;
                    currentPlayer.Level = targetLevel;

                    SendMessage(targetPlayer.Username + " and " + currentPlayer.Username + " have traded pokemon.");

                    //Delete the trade
                    trades.Remove(targetPlayer);

                    //Continuing would start a new trade
                    return;
                }

                if (currentPlayer.Pokemon == 0 || targetPlayer.Pokemon == 0)
                {
                    SendMessage("Both users must have pokemon in their party.");
                    return;
                }

                //Add the request
                if (trades.ContainsKey(currentPlayer))
                    trades.Remove(currentPlayer);
                trades.Add(currentPlayer, targetPlayer);

                Timer timeoutTimer = new Timer(1 * 20 * 1000);
                timeoutTimer.Elapsed += (o, i) => RequestTimeout(currentPlayer, targetPlayer, timeoutTimer);
                timeoutTimer.Start();

                //Notify users of trade
                SendMessage(currentPlayer.Username + " has requested a trade with " + targetPlayer.Username);

                SaveSettings();
            }
        }

        void RequestTimeout(Player sender, Player target, Timer timer)
        {
            //If the target trade exists
            if (trades.ContainsKey(sender) && trades[sender] == target)
            {
                //Destroy the current trade
                trades.Remove(sender);
                SendMessage("The request between " + sender.Username + " and " + target.Username + " has timed out.");
            }
            timer.Stop();
        }


        /*
         * Initialises the IRC bot - does not start connection
         * Requires IRC username and password
         */
        public IRCBot(string username, string password, string channel)
        {
            //Sets the channel for other methods to read
            this.channel = channel.ToLower();
            //Sets a default IRC server
            string server = "irc.twitch.tv";

            //Create the arguments for the connection
            ConnectionArgs args = new ConnectionArgs(username, server);
            args.UserName = username;
            args.ServerPassword = password;

            //Initialise connection with the new arguments
            connection = new Connection(args, false, false);
            connection.TextEncoding = new UTF8Encoding();

            //Connect all of the event handlers
            connection.Listener.OnRegistered += OnRegistered;
            connection.Listener.OnPublic += OnPublic;
            connection.Listener.OnJoin += OnJoin;
            connection.Listener.OnPart += OnPart;
            connection.Listener.OnNames += OnNames;
            connection.Listener.OnChannelModeChange += OnChannelModeChange;

            //Load all settings
            LoadAllSettings();
        }

        //Starts running the IRC bot and receiving messages
        public void Run()
        {
            try
            {
                Console.WriteLine("Bot connecting...");

                Identd.Start(connection.connectionArgs.UserName);

                //Starts the IRC connection
                connection.Connect();
            }
            catch (Exception ex)
            {
                Console.WriteLine("An exception has occurred. (Run)");
                Console.WriteLine(ex.Message);
            }
        }

        //Handles connection registering - connects to channel
        public void OnRegistered()
        {
            try
            {
                Console.WriteLine("Bot connected to chat!");

                Identd.Stop();

                //Join the IRC channel
                connection.Sender.Join(channel.ToLower());

                //Creates the new points timer
                pointsTimer = new Timer(10 * 1000);
                pointsTimer.Elapsed += PointsTimerElapsed;
                pointsTimer.Start();

            }
            catch (Exception ex)
            {
                Console.WriteLine("An exception has occurred. (OnRegistered)");
                Console.WriteLine(ex.Message);
            }
        }

        /*
         * Method that handles users joining the chat
         * user: An object containing information about the user that joined
         * channel: The channel that the new user joined
         */
        void OnJoin(UserInfo user, string channel)
        {
            //Checks that the user that connected was not the bot
            if (connection.connectionArgs.UserName.ToLower() != user.User.ToLower())
                // SendMessage(user.User + " has joined the chat, and is now earning PokéPoints.");
                //SendMessage("If you would like to play with us you can go to the Health and Fitness Chat Room In Pokemon Showdown");

                //Check if we are already tracking user points, otherwise add them as 0 points
                if (!players.ContainsKey(user.User.ToLower()))
                    players.Add(user.User.ToLower(), new Player(user.User));
            players[user.User.ToLower()].Tracking = true;
        }

        /*
                 * Method that handles users leaving the chat
                 * user: An object containing information about the user that leaving
                 * channel: The channel that the new user leaving
                 */
        void OnPart(UserInfo user, string channel, string reason)
        {
            //Check if we are already tracking user points
            players[user.User.ToLower()].Tracking = false;
        }

        /*
         * Handles an initial list of pre-connected users
         * channel: The channel which the users are connected
         * nicks: The users connected to the channel
         * last: The last set of names
         */
        void OnNames(string channel, string[] nicks, bool last)
        {
            //Add each name to the point list
            foreach (string name in nicks)
            {
                if (!players.ContainsKey(name.ToLower()))
                    players.Add(name.ToLower(), new Player(name));
                players[name.ToLower()].Tracking = true;
            }
        }

        void OnChannelModeChange(UserInfo who, string channel, ChannelModeInfo[] modes)
        {
            foreach (ChannelModeInfo mode in modes)
            {
                string user = mode.Parameter;
                if (mode.Mode == ChannelMode.ChannelOperator)
                {
                    if (mode.Action == ModeAction.Add)
                        moderators.Add(user.ToLower());
                    else if (mode.Action == ModeAction.Remove)
                    {
                        if (moderators.Contains(user.ToLower()))
                            moderators.Remove(user.ToLower());
                    }
                }
            }
        }

        /*
         * Every tick of the points timer, adds points to each user
         */
        void PointsTimerElapsed(object sender, ElapsedEventArgs e)
        {
            foreach (Player player in players.Values)
                if (player.Tracking)
                    player.Points += 1;
            SaveSettings();
        }

        //Loads all of the settings into the program
        void LoadAllSettings()
        {
            //Reads the configuration file into a string
            StreamReader reader = new StreamReader("players.config");
            string settingsFile = reader.ReadToEnd();
            reader.Close();

            //Deserialises the configuration into a variable
            players = JsonConvert.DeserializeObject<Dictionary<string, Player>>(settingsFile);

            //Reads the pokemon file into a string
            StreamReader pokemonReader = new StreamReader("pokemon.config");
            string pokemonFile = pokemonReader.ReadToEnd();
            pokemonReader.Close();

            //Deserialises the pokemon into a variable
            dynamic pokemonList = JsonConvert.DeserializeObject(pokemonFile);

            foreach (dynamic pokemon in pokemonList.pokemon)
                this.pokemon.Add((int)pokemon.id, (string)pokemon.name);
        }

        //Saves the settings into the points configuration file
        void SaveSettings()
        {
            StreamWriter streamWriter = new StreamWriter("players.config");
            JsonWriter writer = new JsonTextWriter(streamWriter);
            writer.Formatting = Formatting.Indented;

            //Writes to the settings file
            JsonSerializer serialiser = new JsonSerializer();
            serialiser.Serialize(writer, players);

            writer.Close();
            streamWriter.Close();
        }

        /*
         * Sends a message through to the IRC with /me appended
         * channel: The channel to send the message to
         * message: The message to be sent
         */
        void SendMessage(string message)
        {
            connection.Sender.PublicMessage(channel, "/me " + message);
        }
    }
}