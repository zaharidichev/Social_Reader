package api.userDataPersistance.interfaces;

import org.omg.PortableInterceptor.USER_EXCEPTION;

import api.userDataPersistance.PreferenceType;

/**
 * An implementation of this interface expresses a concept that binds a
 * preference of a user to a particular article
 * 
 * @author 120010516
 * 
 */
public interface IPreference {

	/**
	 * Retrieves the name of the user who expressed this preference
	 * 
	 * @return {@link String}g name
	 */
	public String getUser();

	/**
	 * Retrieves the UID of the content item
	 * 
	 * @return {@link String} uid
	 */
	public String getItemUID();

	/**
	 * Retrieves the type of preference being expressed (ie: like, dislike)
	 * 
	 * @return {@link PreferenceType}
	 */
	public PreferenceType getType();

}
