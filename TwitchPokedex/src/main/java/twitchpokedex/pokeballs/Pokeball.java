package twitchpokedex.pokeballs;

public enum Pokeball
{
	UltraBall(1),
	SafariBall(2);
	
	private final int id;
	Pokeball(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
}
