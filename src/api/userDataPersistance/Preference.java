package api.userDataPersistance;

import api.userDataPersistance.interfaces.IPreference;

/**
 * An implementation of the {@link IPreference} interface to allow the
 * expression of likes and dislikes by a particular user for a particular
 * {@link ItemType#}
 * 
 * @author 120010516
 * 
 */
public class Preference implements IPreference {

	private String user;
	private String UID;
	private PreferenceType type;

	/**
	 * Constructor providing the arguments to build a {@link Preference}
	 * 
	 * @param itemUID
	 *            // the id of the item
	 * @param user
	 *            // the user who expresses this preference
	 * @param type
	 *            // the type of the preference
	 */
	public Preference(String itemUID, String user, PreferenceType type) {
		this.user = user;
		this.UID = itemUID;
		this.type = type;
	}

	@Override
	public String getUser() {
		return this.user;
	}

	@Override
	public String getItemUID() {
		return this.UID;
	}

	@Override
	public PreferenceType getType() {
		return this.type;
	}

}
