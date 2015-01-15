package twitchpokedex.utils;

import java.util.List;
import java.util.Random;

import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.Type;
import twitchpokedex.items.Pokeball;

public class PokePicker
{
	/**
	 * Picks a random pokemon using a specific pokeball
	 * 
	 * @param pokeball The pokeball to use
	 * @return The random pokemon
	 */
	public static Pokemon GetRandomPokemon(Pokeball ball)
	{
		List<Pokemon> selectionPokemon;
		if (ball == Pokeball.normalball || ball == Pokeball.greatball
				|| ball == Pokeball.ultraball)
			selectionPokemon = DBConn.GetAllPokemon();
		else
			selectionPokemon = GetAllType(ball.getPokemonType());

		Random random = new Random();

		Pokemon newPokemon = selectionPokemon.get(random
				.nextInt(selectionPokemon.size()));

		if (newPokemon.isLegendary())
			// 1 in 3 odds of throwing away your legendary for a new random
			// pokemon
			if (OneInNOdds(3))
				newPokemon = selectionPokemon.get(random
						.nextInt(selectionPokemon.size()));

		return newPokemon;
	}

	/**
	 * Selects all pokemon of a certain type
	 * 
	 * @param type The type to select
	 * @return A list of all pokemon of that type
	 */
	private static List<Pokemon> GetAllType(String type)
	{
		Type pokemonType = DBConn.GetType(type);
		return DBConn.GetAllPokemon(pokemonType);
	}

	/**
	 * Produces a 1-in-n odds and returns whether it was produced
	 * 
	 * @param n The 1-in-n n to produce for
	 * @return Whether it could produce the result of those odds
	 */
	private static boolean OneInNOdds(int n)
	{
		Random rng = new Random();
		return (rng.nextInt(n) == 0);
	}
}
