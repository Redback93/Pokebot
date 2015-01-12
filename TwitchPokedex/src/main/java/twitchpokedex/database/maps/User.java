package twitchpokedex.database.maps;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User extends MapModel
{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private int twitchId;
	private String username;
	private String displayName;
	private int points;
	private int donator;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="timestamp", nullable = false,
	    columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
	private Date updatedAt = new Date();

	public static User Create(int twitchId, String username, String displayName)
	{
		User newUser = new User();
		
		newUser.twitchId = twitchId;
		newUser.username = username;
		newUser.displayName = displayName;
		newUser.points = 0;
		newUser.donator = 0;
		
		return newUser;
	}
	
	public boolean isDefault()
	{
		return this.id == null;
	}

	public void addPoints(int points)
	{
		this.points += points;
	}

	public void removePoints(int points)
	{
		this.points -= points;
	}

	public boolean hasDonated()
	{
		return getDonator()>0;
	}
}
