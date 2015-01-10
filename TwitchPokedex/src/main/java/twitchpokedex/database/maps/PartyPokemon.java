package twitchpokedex.database.maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PartyPokemon implements java.io.Serializable, Comparable<PartyPokemon>
{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private int user;
	private int slot;
	private int pokemon;
	private int level;
	private String name;
	private boolean mega;
	private boolean shiny;

	@Override
	public int compareTo(PartyPokemon o)
	{
		return this.getSlot() - o.getSlot();
	}
}
