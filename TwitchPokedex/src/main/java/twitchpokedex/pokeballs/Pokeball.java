package twitchpokedex.pokeballs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor
public enum Pokeball
{
	UltraBall(1),
	SafariBall(2);
	
	private final int id;
}
