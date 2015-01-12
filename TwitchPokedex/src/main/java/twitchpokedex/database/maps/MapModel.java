package twitchpokedex.database.maps;

import twitchpokedex.database.DBConn;

public class MapModel implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;

	public void update()
	{
		DBConn.UpdateObject(this);
	}
	
	public void save()
	{
		DBConn.SaveOrUpdateObject(this);
	}
}
