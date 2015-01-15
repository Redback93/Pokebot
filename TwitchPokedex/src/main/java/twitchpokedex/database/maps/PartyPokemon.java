package twitchpokedex.database.maps;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PartyPokemon extends MapModel implements Comparable<PartyPokemon>
{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private int user;
	private int slot;
	private Pokemon pokemon;
	private int level;
	private String name;
	private boolean mega;
	private boolean shiny;

	@Override
	public int compareTo(PartyPokemon o)
	{
		return this.getSlot() - o.getSlot();
	}

	public void levelUp()
	{
		this.level++;
	}

	public String getDisplayName()
	{
		if (name == null || StringUtils.isEmpty(name))
			return pokemon.getName();
		else
			return String.format("%s (%s)", pokemon.getName(), this.name);
	}
}
