package twitchpokedex.logging;

import org.slf4j.LoggerFactory;

public class Logging
{
	private static final org.slf4j.Logger activeLogger = LoggerFactory.getLogger(Logging.class);
	
	/**
	 * Selects the current active logger
	 * @return The active, configured logger
	 */
	public static org.slf4j.Logger GetLogger()
	{
		return activeLogger;
	}
}
