package twitchpokedex.items;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor
public enum Pokeball implements Item
{
	pokeball(""), // Everything
	greatball(""), //Increased legendary chance
	ultraball(""), //More increased legendary chance
	normalball("normal"), // Normal
	dojoball("fighting"), // Fighting
	lavaball("fire"), // Fire
	wingball("flying"), // Flying
	leafball("grass"), // Grass
	toxicball("poison"), // Poison
	earthball("ground"), // Ground
	stoneball("rock"), // Rock
	netball("bug"), // Bug
	phantomball("ghost"), // Ghost
	ironball("steel"), // Steel
	diveball("water"), // Water
	thunderball("electric"), // Electric
	mindball("psychic"), // Psychic
	frostball("ice"), // Ice
	shadowball("dark"), // Dark
	dragonball("dragon"), // Dragon
	fairyball("fairy"); // Fairy

	private String pokemonType;

	@Override
	public String description()
	{
		return ItemManager.getItemDescription(identifier());
	}

	@Override
	public String displayName()
	{
		return ItemManager.getItemName(identifier());
	}

	@Override
	public String identifier()
	{
		return name();
	}
}
