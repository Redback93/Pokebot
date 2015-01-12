package twitchpokedex.database;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import twitchpokedex.database.maps.Cost;
import twitchpokedex.database.maps.MapModel;
import twitchpokedex.database.maps.PartyPokemon;
import twitchpokedex.database.maps.Pokemon;
import twitchpokedex.database.maps.Setting;
import twitchpokedex.database.maps.User;
import twitchpokedex.logging.Logging;

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
				.addResource("hibernate.cfg.xml").configure();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
				cfg.getProperties()).build();
		factory = cfg.buildSessionFactory(serviceRegistry);
	}

	/**
	 * Builds a list of all pokemon in the database
	 * 
	 * @return The list of all pokemon in the database
	 */
	public static List<Pokemon> GetAllPokemon()
	{
		Session session = factory.openSession();
		try {
			Query query = session.createQuery("from Pokemon");
			return (List<Pokemon>) query.list();
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return (Collections.<Pokemon> emptyList());
		} finally {
			session.close();
		}
	}

	/**
	 * Retrieves a setting from the database
	 * 
	 * @param key Given key for the database setting
	 * @return Associated value for the database setting
	 */
	public static String GetSetting(String key)
	{
		Session session = factory.openSession();
		try {
			Setting setting = (Setting) session.get(Setting.class, key);
			return setting.getValue();
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return "";
		} finally {
			session.close();
		}
	}

	/**
	 * Retrieves a pokemon by its given Id
	 * 
	 * @param id Given Id for the pokemon
	 * @return The pokemon associated with that Id or an empty pokemon
	 */
	public static Pokemon GetPokemon(Integer id)
	{
		Session session = factory.openSession();
		try {
			return (Pokemon) session.get(Pokemon.class, id);
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return new Pokemon();
		} finally {
			session.close();
		}
	}

	/**
	 * Retrieves a pokemon by its given name
	 * 
	 * @param name Given name for the pokemon
	 * @return The pokemon associated with that name or an empty pokemon
	 */
	public static Pokemon GetPokemon(String name)
	{
		Session session = factory.openSession();
		try {
			Query query = session
					.createQuery("from Pokemon P where P.name = :name");
			query.setParameter("name", name);
			return (Pokemon) query.list().get(0);
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return new Pokemon();
		} finally {
			session.close();
		}
	}

	/**
	 * Retrieves a user given their username
	 * 
	 * @param user Given username for the user
	 * @return The user associated with the username or an empty user
	 */
	public static User GetUser(String user)
	{
		Session session = factory.openSession();
		try {
			Query query = session
					.createQuery("from User U where U.username = :name");
			query.setParameter("name", user);
			return (User) query.list().get(0);
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return new User();
		} finally {
			session.close();
		}
	}

	/**
	 * Gets a list of all pokemon in a user's party
	 * @param user The user associated with the party
	 * @return The list of pokemon in the user's party sorted in order of slot
	 */
	public static List<PartyPokemon> GetUserParty(User user)
	{
		Session session = factory.openSession();
		try {
			Query query = session
					.createQuery("from PartyPokemon P where P.user = :userid");
			query.setParameter("userid", user.getId());
			List<PartyPokemon> party = (List<PartyPokemon>) query.list();
			//Sort in order of slot - put in order 1-6
			Collections.sort(party);
			return party;
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return Collections.<PartyPokemon> emptyList();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Updates the given object
	 * 
	 * @param model The model to update in the DB
	 */
	public static void UpdateObject(MapModel model)
	{
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.merge(model);
			session.getTransaction().commit();
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Saves a new object or updates an existing object in the database
	 * @param object The object to save or update
	 */
	public static void SaveOrUpdateObject(Object object)
	{
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(object);
			session.getTransaction().commit();
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	
	/**
	 * Gets a cost from the database
	 * @param key The key of the cost
	 * @return The cost associated with that key
	 */
	public static int GetCost(String key)
	{
		Session session = factory.openSession();
		try {
			return ((Cost) session.get(Cost.class, key)).getCost();
		} catch (Exception e) {
			Logging.GetLogger().error(e.getMessage());
			return 0;
		} finally {
			session.close();
		}
	}
}
