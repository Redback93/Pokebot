package twitchpokedex.pokeballs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

@Getter(AccessLevel.PUBLIC)
@AllArgsConstructor
public enum Pokeball
{
	UltraBall(1),
	SafariBall(2),
	FireBall(3);
	
	private final int id;
}
