package twitchpokedex.database;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.Setting;

public class DBConn
{
	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;
	
	/**
	 * Builds and creates a connection to the database
	 */
	public static void Connect()
	{
		Configuration cfg = new Configuration()
		.addResource("hibernate.cfg.xml")
		.configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		factory = cfg.buildSessionFactory(serviceRegistry);
	}

	/**
	 * Builds a list of all pokemon in the database
	 * @return The list of all pokemon in the database
	 */
	public static List<Pokemon> GetAllPokemon()
	{
		Session session = factory.openSession();
		try {
			Query query = session.createQuery("from Pokemon");
			return (List<Pokemon>)query.list();
		}
		catch (Exception e) {
		   e.printStackTrace();
		   return (Collections.<Pokemon>emptyList());
		} finally {
		   session.close();
		}
	}
	
	/**
	 * Retrieves a setting from the database
	 * @param key Given key for the database setting
	 * @return Assosciated value for the database setting
	 */
	public static String GetSetting(String key)
	{
		Session session = factory.openSession();
		try {
			Setting setting = (Setting) session.get(Setting.class, key);
			return setting.getValue();
		}
		catch (Exception e) {
		   e.printStackTrace();
		   return "";
		} finally {
		   session.close();
		}
	}
	
	/**
	 * Retrieves a pokemon by its given Id
	 * @param id Given Id for the pokemon
	 * @return The pokemon assosciated with that Id
	 */
	public static Pokemon GetPokemonById(Integer id)
	{
		Session session = factory.openSession();
		try {
			return (Pokemon) session.get(Pokemon.class, id);
		}
		catch (Exception e) {
		   e.printStackTrace();
		   return new Pokemon();
		} finally {
		   session.close();
		}
	}
	
	/**
	 * Retrieves a pokemon by its given name
	 * @param name Given name for the pokemon
	 * @return The pokemon assosciated with that name
	 */
	public static Pokemon GetPokemonByName(String name)
	{
		Session session = factory.openSession();
		try {
			Query query = session.createQuery("from Pokemon P where P.name = :name");
			query.setParameter("name", name);
			return (Pokemon) query.list().get(0);
		}
		catch (Exception e) {
		   e.printStackTrace();
		   return new Pokemon();
		} finally {
		   session.close();
		}
	}
}