package twitchpokedex.items;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor
public enum Usable implements Item
{
	shiny_stone, mega_stone;

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
