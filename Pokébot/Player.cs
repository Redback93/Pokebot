using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Newtonsoft.Json;

namespace Pokébot
{
    class Player
    {
        public string Username;
        public int Points;
        public int Level;
        public int Pokemon;
        [JsonIgnore]
        public bool Tracking;

        public Player(string username)
        {
            Username = username;
            Points = 0;
            Level = 0;
            Pokemon = 0;
            Tracking = true;
        }
    }
}
