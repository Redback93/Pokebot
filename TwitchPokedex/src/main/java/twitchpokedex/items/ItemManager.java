package twitchpokedex.items;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import twitchpokedex.logging.Logging;
import twitchpokedex.utils.Localisation;

public class ItemManager
{
	private static ResourceBundle items;
	/**
	 * Maps display names to their identifiers
	 */
	private static Map<String, String> identifiers = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

	/**
	 * Maps identifier prefixes to their classes
	 */
	private static HashMap<String, Class> prefixes = new HashMap<String, Class>();
	static {
		prefixes.put("pokeball", Pokeball.class);
		prefixes.put("usable", Usable.class);
	}

	public static void LoadItems()
	{
		items = ResourceBundle.getBundle("twitchpokedex.localisation.Items",
				Localisation.getLocale());

		Enumeration<String> keys = items.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (!key.endsWith("description") && !key.startsWith("default"))
				identifiers.put(items.getString(key), key);
		}
		Logging.GetLogger().info(String.format("Loaded %d items", identifiers.size()));
	}

	private static String tryGetString(String keyName, String defaultReturn)
	{
		if (items.containsKey(keyName))
			return keyName;
		else
			return defaultReturn;
	}

	public static String getItemName(String identifier)
	{
		return tryGetString("items.name." + identifier, "items.name.default");
	}

	public static String getItemDescription(String identifier)
	{
		return tryGetString("items.description." + identifier,
				"items.description.default");
	}

	public static Item getItem(String displayName)
	{
		String identifier = identifiers.get(displayName);
		if (identifier == null)
			return null;

		String[] splitIdentifier = StringUtils.split(identifier, ".");
		Class enumClass = prefixes.get(splitIdentifier[0]);
		return (Item) Enum.valueOf(enumClass, splitIdentifier[1]);
	}
}
