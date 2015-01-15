package twitchpokedex.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.AccessLevel;
import lombok.Getter;

public class Localisation
{
	@Getter(AccessLevel.PUBLIC)
	private static Locale locale;
	private static ResourceBundle responses;

	public static void SetLocale(String language)
	{
		SetLocale(language, "");
	}

	public static void SetLocale(String language, String country)
	{
		locale = new Locale(language, country);
		responses = ResourceBundle.getBundle(
				"twitchpokedex.localisation.Responses", locale);
	}

	public static String getString(String keyName)
	{
		return responses.getString(keyName);
	}

	public static String tryGetString(String keyName, String defaultReturn)
	{
		if (responses.containsKey(keyName))
			return keyName;
		else
			return defaultReturn;
	}

	public static String getString(String keyName, Object... args)
	{
		return String.format(getString(keyName), args);
	}

	public static String getFormat(String keyName)
	{
		return getString("format.Correct", getString(keyName));
	}
}
