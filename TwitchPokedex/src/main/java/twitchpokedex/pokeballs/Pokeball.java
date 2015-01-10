package twitchpokedex.pokeballs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor
public enum Pokeball
{
	Normalball(1), //Normal
	Dojoball(2), //Fighting
	LavaBall(3), //Fire
	Wingball(4), //Flying
	Leafball(5), //Grass
	Toxicball(6), //Poison
	Earthball(7), //Ground
	Stoneball(8), //Rock
	Netball(9), //Bug
	Phantomball(10), //Ghost
	Ironball(11), //Steel
	Diveball(12), //Water
	Thunderball(13), //Electric
	Mindball(14), //Psychic
	Frostball(15), //Ice
	Shadowball(16), //Dark
	Dragonball(17), //Dragon
	Fairyball(18); //Fairy
	
	private final int id;
}
