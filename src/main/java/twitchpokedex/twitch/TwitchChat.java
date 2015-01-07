package twitchpokedex.twitch;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import twitchpokedex.twitch.handlers.GenericListener;
import twitchpokedex.twitch.handlers.ModeratorListener;
import twitchpokedex.twitch.handlers.TradeListener;

public class TwitchChat
{
	private PircBotX _bot;
	
	public TwitchChat()
	{
		Configuration<PircBotX> config = new Configuration.Builder<PircBotX>()
			.setName("twitchpokedex")
			.setLogin("twitchpokedex")
			.setAutoNickChange(false)
			.setServerPassword("oauth:db3u38mg1305rkdighyq1tzlsri181")
			.setServer("irc.twitch.tv", 6667)
			.addAutoJoinChannel("#redback93")
			.addListener(new GenericListener())
			.addListener(new TradeListener())
			.addListener(new ModeratorListener())
			.buildConfiguration();

		_bot = new PircBotX(config);
	}
	
	public void Start() throws Exception 
	{
		_bot.startBot();
	}
	
	public void Stop()
	{
		_bot.stopBotReconnect();
	}
}
