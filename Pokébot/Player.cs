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
            this.Username = username;
            this.Points = 0;
            this.Level = 0;
            this.Pokemon = 0;
            this.Tracking = true;
        }
    }
}
