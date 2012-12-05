package api.userDataPersistance.interfaces;

import java.rmi.server.UID;
import java.util.LinkedList;

import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.PreferenceType;
import exceptions.ExistingUserException;
import exceptions.InvalidCredentialsException;

/**
 * This is an interface to the implementation of a database connections. The
 * database in this case is a persistent storage for user metadata. Things such
 * as Likes, Annotations and favorites are stored in this database. The database
 * needs to be a NON sql storage, employing the concepts of bags and entities
 * 
 * @author 120010516
 * 
 */
public interface IDBConnection{

	/**
	 * Given a name of a Bag, it checks whether the bag exists in the database
	 * 
	 * @param bagName
	 *            {@link String} the name of the bag
	 * @return
	 * @throws RestFulClientException
	 */
	boolean doesBagExist(String bagName) throws RestFulClientException;

	/**
	 * Given a username, this method checks whether it already exists in the
	 * database
	 * 
	 * @param username
	 *            {@link StringIndexOutOfBoundsException} the username
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	boolean doesUserExist(String username) throws RestFulClientException;

	/**
	 * Given an id of an item, the method checks whether it exists as a
	 * bookmarked item for a particular user
	 * 
	 * @param item
	 *            the ID
	 * @param user
	 *            the username
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	boolean isInFavourites(IResultItem item, String user)
			throws RestFulClientException;

	/**
	 * Given a username, a list of {@link IResultItem} is returnes. Those items
	 * are the ones that the user marked as favourites
	 * 
	 * @param username
	 *            the username
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	LinkedList<IResultItem> retreiveFavourites(String username)
			throws RestFulClientException;

	/**
	 * Given a username and a password, this method checks whether the
	 * credentials are valid. Security and encryption is left to the
	 * implementation
	 * 
	 * @param name
	 *            the username
	 * @param password
	 *            the password
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	boolean areCredentialsValid(String name, String password)
			throws RestFulClientException;

	/**
	 * Given an {@link IPreference} object, this method publishes it to the
	 * database, thus recording the preference of a particular user for a
	 * particular piece of content
	 * 
	 * @param preference
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	void expressPreference(IPreference preference)
			throws RestFulClientException;

	/**
	 * Given a UID of an item and the type of the preference, this method
	 * returns the number of Likes or dislikes that were published for this item
	 * 
	 * @param UID
	 *            the {@link UID} of the article
	 * @param type
	 *            {@link PreferenceType} the type that we are looking for
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	int getNumberOfPreferences(String UID, PreferenceType type)
			throws RestFulClientException;

	/**
	 * This method logs the user into the database conenction, so one can
	 * publish annotations and prefernces, as well as add favourites
	 * 
	 * @param user
	 *            the username
	 * @param password
	 *            the password
	 * 
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 * @throws exceptions.InvalidCredentialsException
	 *             in case the credentials were invalid
	 */
	void login(String user, String password)
			throws InvalidCredentialsException, RestFulClientException,
			exceptions.InvalidCredentialsException;

	/**
	 * This method creates a bag with the specified name in the database
	 * 
	 * @param bagName
	 *            {@link String} the name of the bag
	 * @throws RestFulClientException
	 */
	void createBag(String bagName) throws RestFulClientException;

	/**
	 * This method adds a user to the database. Encryption is left to the
	 * implementer
	 * 
	 * @param name
	 *            the username
	 * @param pass
	 *            the password
	 * @throws ExistingUserException
	 *             in vase the user already exists
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	void addUser(String name, String pass) throws ExistingUserException,
			RestFulClientException;

	/**
	 * Given an {@link IResultItem} and a username, this method adds all the
	 * data to the database storage, thus forming a list of bookmarks for a
	 * particular user
	 * 
	 * @param item
	 *            the item
	 * @param user
	 *            the username
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	void addItemToFavourites(IResultItem item, String user)
			throws RestFulClientException;

	/**
	 * This method adds an annotation to the database. The annotation object
	 * contains all the needed data for determining who issues the annotation
	 * and to what particular item it refers
	 * 
	 * @param annotation
	 *            {@link IAnnotation} object
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	void addAnnotation(IAnnotation annotation) throws RestFulClientException;

	/**
	 * This method retrieves all the annotations for a particular item given its
	 * ID
	 * 
	 * @param UID
	 *            the ID of the item
	 * @return
	 * @throws RestFulClientException
	 *             in case something went wrong with the connection to the
	 *             database
	 */
	LinkedList<IAnnotation> getAnnotationsForItem(String UID)
			throws RestFulClientException;

	/**
	 * Retrieves the logged user
	 * 
	 * @return the name of the user
	 */
	String getLoggedUser();

	/**
	 * Given a name of a bag, create it in the database if and only if it is not
	 * existing already
	 * 
	 * @param bagName
	 * @throws RestFulClientException
	 */
	void createBagIfNotThere(String bagName) throws RestFulClientException;

	/**
	 * Given a nema of a Bag, create it in the database
	 * 
	 * @param jsonData
	 * @param bagName
	 * @throws RestFulClientException
	 */
	void addContentToBag(String jsonData, String bagName)
			throws RestFulClientException;

}
