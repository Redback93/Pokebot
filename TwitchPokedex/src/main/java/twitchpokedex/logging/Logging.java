package twitchpokedex.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Logging
{
	private static final Logger activeLogger = Logger.getLogger(Logging.class);
	
	/**
	 * Selects the current active logger
	 * @return The active, configured logger
	 */
	public static Logger GetLogger()
	{
		return activeLogger;
	}
}
