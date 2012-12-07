package tests;

import org.junit.Before;
import org.junit.Test;

import api.userDataPersistance.Preference;
import api.userDataPersistance.PreferenceType;
import api.userDataPersistance.interfaces.IPreference;

/**
 * Class to test the functionality of the {@link Preference} objects. It is
 * mostly getters
 * 
 * @author 120010516
 * 
 */
public class UserPreferenceTesting {

	private IPreference preference; // holds the tested preference

	@Before
	public void init() {
		this.preference = new Preference("ID", "asd", PreferenceType.like);
	}

	@Test
	public void testGettingUser() {
		org.junit.Assert.assertTrue(this.preference.getUser().equals("asd"));
	}

	@Test
	public void testGettingID() {
		org.junit.Assert.assertTrue(this.preference.getItemUID().equals("ID"));
	}

	@Test
	public void testPREFType() {
		org.junit.Assert.assertEquals(PreferenceType.like,
				this.preference.getType());
	}
}
