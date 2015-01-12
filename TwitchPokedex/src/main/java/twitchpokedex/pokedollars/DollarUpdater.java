package twitchpokedex.pokedollars;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import twitchpokedex.database.DBConn;
import twitchpokedex.database.maps.User;

public class DollarUpdater
{
	/**
	 * Contains the users who are currently earning points
	 */
	private static List<User> updatingUsers = new ArrayList<User>();
	
	private static Timer updatingTimer;
	
	/**
	 * Adds an IRC user to the list of updating Twitch pokedex
	 * @param channelUser The IRC user to add
	 */
	public static void AddUser(User newUser)
	{	
		if(!updatingUsers.contains(newUser)) updatingUsers.add(newUser);
	}
	
	/**
	 * Starts the process of updating the users
	 */
	public static void StartUpdating()
	{
		updatingTimer = new Timer();
		updatingTimer.scheduleAtFixedRate(new DollarUpdateTask(), 0, Integer.parseInt(DBConn.GetSetting("PointTimer")));
	}
	
	/**
	 * Stops updating the users
	 */
	public static void StopUpdating()
	{
		updatingTimer.cancel();
	}
	
	static class DollarUpdateTask extends TimerTask 
	{
		public void run() 
		{
			int newPoints = Integer.parseInt(DBConn.GetSetting("AccumulatedPoints"));
			for(User user : updatingUsers)
			{
				user.addPoints(newPoints);
				user.update();
			}
		}
	}
}
