package twitchpokedex.twitch;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.charset.Charset;
import java.util.Collections;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;

import twitchpokedex.constants.Constants;
import twitchpokedex.database.DBConn;
import twitchpokedex.logging.Logging;
import twitchpokedex.twitch.listeners.ChannelListener;
import twitchpokedex.twitch.listeners.GenericListener;
import twitchpokedex.twitch.listeners.TradeListener;

/**
 * Manages the connection to the Twitch IRC channels
 * @author Redback
 *
 */
public class TwitchChat
{
	private Thread runningThread;
	private PircBotX _bot;

	public TwitchChat()
	{
		this(new String[0]);
	}
	
	/**
	 * Constructs the Twitch chat with a list of channels to join upon connection
	 * @param initialChannels The list of channel names to connect to
	 */
	public TwitchChat(String[] initialChannels)
	{
		_bot = new PircBotX(buildConfigure(initialChannels));
		User.setDonatorIcon(Constants.DONATOR_ICON);
	}

	/**
	 * Begin connection to the IRC server 
	 */
	public void Start()
	{
		runningThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try 
				{	
					_bot.startBot();
				} 
				catch (InterruptedIOException e)
				{
					Logging.GetLogger().info("IRC Bot Disconnected");
				} catch (IOException e) 
				{
					Logging.GetLogger().error(e.getMessage());
				} catch (IrcException e) 
				{
					Logging.GetLogger().error(e.getMessage());
				} 
			}
		});
		runningThread.start();
	}

	/**
	 * Disconects the bot from the chat
	 */
	public void Stop()
	{
		_bot.stopBotReconnect();
		runningThread.interrupt();
	}

	/**
	 * Connects the bot to a given channel
	 * @param channelName An unformatted channel name
	 */
	public void JoinChannel(String channelName)
	{
		_bot.sendRaw().rawLine("/join " + formatChannel(channelName));
		System.out.println("joining " + channelName);
	}

	/**
	 * Formats a username or channel name to match IRC standards
	 * @param channelName The unformatted channel name
	 * @return An IRC standard channel name
	 */
	private String formatChannel(String channelName)
	{
		// Must be prefixed with a pound sign
		if (!channelName.startsWith("#"))
			channelName = "#" + channelName;

		// Must be in lowercase form
		return channelName.toLowerCase();
	}

	 /**
	 * Creates the configuration for the IRCBot
	 * @param channels A list of channels to join after connecting
	 * @return The created configuration
	 */
	private Configuration<PircBotX> buildConfigure(String[] channels)
	{
		String username = DBConn.GetSetting("TwitchUsername");
		String password = DBConn.GetSetting("TwitchPassword");
		Configuration.Builder<PircBotX> config = new Configuration.Builder<PircBotX>()
				.setName(username).setLogin(username).setAutoNickChange(false)
				.setServerPassword(password)
				.setServer(Constants.IRC_HOST, Constants.IRC_PORT)
				.setEncoding(Charset.forName("UTF-8"))
				.addListener(new GenericListener())
				.addListener(new TradeListener())
				.addListener(new ChannelListener());
		for(String channel : channels)
			config.addAutoJoinChannel(formatChannel(channel));

		return config.buildConfiguration();
	}
}
