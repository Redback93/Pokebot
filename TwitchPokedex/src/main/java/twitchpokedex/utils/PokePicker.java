package twitchpokedex.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.pokeballs.Pokeball;

public class PokePicker
{
	/**
	 * Picks a random pokemon using a specific pokeball
	 * @param pokeball The pokeball to use
	 * @return The random pokemon
	 */
	public static Pokemon GetRandomPokemon(Pokeball ball)
	{
		List<Pokemon> selectionPokemon;
		switch(ball)
		{
			case UltraBall:
				selectionPokemon = DBConn.GetAllPokemon();
				break;
			case SafariBall:
				selectionPokemon = GetAllBugType();
				break;
			default:
				selectionPokemon = DBConn.GetAllPokemon();
				break;
		}
		
		Random random = new Random();
		
		Pokemon newPokemon = selectionPokemon.get(random.nextInt(selectionPokemon.size()));
		
		if(newPokemon.isLegendary())
			//1 in 3 odds of throwing away your legendary for a new random pokemon
			if(OneInNOdds(3))
				newPokemon = selectionPokemon.get(random.nextInt(selectionPokemon.size()));
		
		return newPokemon;
	}
	
	/**
	 * Selects all pokemon which are bug type
	 * @return A list of all bug type pokemon
	 */
	private static List<Pokemon> GetAllBugType()
	{
		return Collections.<Pokemon>emptyList();
	}
	
	/**
	 * Produces a 1-in-n odds and returns whether it was produced
	 * @param n The 1-in-n n to produce for
	 * @return Whether it could produce the result of those odds
	 */
	private static boolean OneInNOdds(int n)
	{
		Random rng = new Random();
		return (rng.nextInt(n) == 0);
	}
}
