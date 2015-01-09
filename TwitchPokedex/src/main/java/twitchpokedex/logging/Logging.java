package twitchpokedex.logging;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Logging
{
	private static final org.slf4j.Logger activeLogger = LoggerFactory.getLogger(Logging.class);
	
	public static final Marker DATABASE_ERROR = MarkerFactory.getMarker("DATABASE_ERROR");
	
	/**
	 * Selects the current active logger
	 * @return The active, configured logger
	 */
	public static org.slf4j.Logger GetLogger()
	{
		return activeLogger;
	}
}
