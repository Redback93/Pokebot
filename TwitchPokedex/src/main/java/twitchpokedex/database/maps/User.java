package twitchpokedex.database.maps;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import twitchpokedex.database.DBConn;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private int twitchId;
	private String username;
	private String displayName;
	private int points;
	private int donator;
	private Date updatedAt;

	public boolean isDefault()
	{
		return this.id == 0;
	}
	
	public void Save()
	{
		DBConn.UpdateUser(this);
	}
}
