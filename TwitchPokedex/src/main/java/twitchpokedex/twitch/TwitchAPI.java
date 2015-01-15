package twitchpokedex.twitch;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import twitchpokedex.logging.Logging;
import twitchpokedex.twitch.api.TwitchUser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TwitchAPI
{
	private static final String USER_API = "https://api.twitch.tv/kraken/users/%s";

	private static <T> T GetObject(String url, Class<T> type)
	{
		URL getRequest;
		try {
			getRequest = new URL(url);
			try (Reader reader = new InputStreamReader(getRequest.openStream())) {
				Gson gson = new GsonBuilder().create();
				T returnObject = gson.fromJson(reader, type);
				return returnObject;
			}
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return null;
		}
	}

	/**
	 * Gets a Twitch user from the API
	 * 
	 * @param username The username of the Twitch user
	 * @return The object representing the Twitch user
	 */
	public static TwitchUser GetTwitchUser(String username)
	{
		String url = String.format(USER_API, username);
		TwitchUser returnUser = GetObject(url, TwitchUser.class);

		return returnUser;
	}
}
