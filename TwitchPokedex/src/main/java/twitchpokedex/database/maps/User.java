package twitchpokedex.database.maps;

// Generated 08/01/2015 2:48:06 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * Users generated by hbm2java
 */
public class User implements java.io.Serializable
{

	private Integer id;
	private Pokemon pokemon;
	private int twitchId;
	private String username;
	private String displayName;
	private int points;
	private int level;
	private Date updatedAt;

	public User()
	{
	}

	public User(Pokemon pokemon, int twitchId, String username,
			String displayName, int points, int level, Date updatedAt)
	{
		this.pokemon = pokemon;
		this.twitchId = twitchId;
		this.username = username;
		this.displayName = displayName;
		this.points = points;
		this.level = level;
		this.updatedAt = updatedAt;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Pokemon getPokemon()
	{
		return this.pokemon;
	}

	public void setPokemon(Pokemon pokemon)
	{
		this.pokemon = pokemon;
	}

	public int getTwitchId()
	{
		return this.twitchId;
	}

	public void setTwitchId(int twitchId)
	{
		this.twitchId = twitchId;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public int getPoints()
	{
		return this.points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public int getLevel()
	{
		return this.level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public Date getUpdatedAt()
	{
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}

}
