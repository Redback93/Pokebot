package twitchpokedex.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localisation
{
	private static Locale locale;
	private static ResourceBundle responses;
	
	public static void SetLocale(String language)
	{
		SetLocale(language, "");
	}
	
	public static void SetLocale(String language, String country)
	{
		locale = new Locale(language, country);
		responses = ResourceBundle.getBundle("twitchpokedex.localisation.Responses", locale);
	}
	
	public static String getString(String keyName)
	{
		return responses.getString(keyName);
	}
	
	public static String getString(String keyName, Object... args)
	{
		return String.format(getString(keyName), args);
	}
}
