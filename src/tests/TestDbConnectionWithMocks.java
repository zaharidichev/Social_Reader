package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.Utils;
import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.restful.IRESTFULClient;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * 
 * This set of tests ensures that the non trivial functionality of the
 * Implementation of {@link IDBConnection} class is working as expected. The
 * class shares a complex relationship with the {@link RestFulClient}. IN order
 * to break up this relationship Jmock is used to mock up this restful
 * interface. Additionally local files saved on disk are used to store the Json
 * data that should be retrieved in order for the {@link DBConnection} to work.
 * 
 * @author 120010516
 * 
 */
@RunWith(JMock.class)
public class TestDbConnectionWithMocks {

	// the variables storing the Json data as read from disk
	private InputStream existingBagData;
	private InputStream notExistingBagData;
	private InputStream nonExistingUser;
	private InputStream existingUser;
	private InputStream validCredentials;
	private InputStream invalidUserCredentials;
	private InputStream favouritesForAUserKOSTA;
	private InputStream noFavouritesFound;
	private InputStream sixLikesData;
	private InputStream annotationsForKnownUID;

	private IDBConnection connection; // the db connection that is being tested
	private final Mockery context = new JUnit4Mockery(); // the mockery
	private IRESTFULClient restFulClient; // the mock of the restful client

	@Before
	public void init() throws RestFulClientException {

		/*
		 * This block of code here reads the Json data from disk in order to
		 * emulate predictable behaviour for the mocked restful interface.
		 */
		existingBagData = new ByteArrayInputStream(
				Utils.getMockResponse("resources/existingBag.txt"));
		notExistingBagData = new ByteArrayInputStream(
				Utils.getMockResponse("resources/notExistingBag.txt"));

		nonExistingUser = new ByteArrayInputStream(
				Utils.getMockResponse("resources/nonExistingUserReply.txt"));
		existingUser = new ByteArrayInputStream(
				Utils.getMockResponse("resources/existingUserReply.txt"));

		validCredentials = new ByteArrayInputStream(
				Utils.getMockResponse("resources/validCredentials.txt"));

		invalidUserCredentials = new ByteArrayInputStream(
				Utils.getMockResponse("resources/invalidUserCredentials.txt"));
		favouritesForAUserKOSTA = new ByteArrayInputStream(
				Utils.getMockResponse("resources/favouritesForUser.txt"));

		noFavouritesFound = new ByteArrayInputStream(
				Utils.getMockResponse("resources/noFavourites.txt"));

		sixLikesData = new ByteArrayInputStream(
				Utils.getMockResponse("resources/sixLikes.txt"));

		annotationsForKnownUID = new ByteArrayInputStream(
				Utils.getMockResponse("resources/annotationsForKnownUID.txt"));

		restFulClient = context.mock(IRESTFULClient.class);

		/*
		 * Here the expectatiosn of the mock are specified. The way this works
		 * is by predicting what inputs will be supplied. After that for a
		 * particular input a piece of data is returned. This data is completely
		 * predictable since it is being stored on disk
		 */
		context.checking(new Expectations() {
			{

				// mocking needed methods
				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Favourites)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(notExisting)");
				will(returnValue(notExistingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Favourites)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Annotations)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Users)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Likes)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Dislikes)");
				will(returnValue(existingBagData));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Users)/Entities(userName=non)");
				will(returnValue(nonExistingUser));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Users)/Entities(userName=kosta)");
				will(returnValue(existingUser));

				allowing(restFulClient)
						.executeRequest(
								"localhost:7050/Bags(Users)/Entities(userName=kosta,passwd=kosta)");
				will(returnValue(validCredentials));

				allowing(restFulClient)
						.executeRequest(
								"localhost:7050/Bags(Users)/Entities(userName=nobody,passwd=really)");
				will(returnValue(invalidUserCredentials));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Favourites)/Entities(user=kosta)");
				will(returnValue(favouritesForAUserKOSTA));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Favourites)/Entities(user=noone)");
				will(returnValue(noFavouritesFound));

				allowing(restFulClient).executeRequest(
						"localhost:7050/Bags(Likes)/Entities(uid=280600449)");
				will(returnValue(sixLikesData));

				allowing(restFulClient)
						.executeRequest(
								"localhost:7050/Bags(Annotations)/Entities(uid=280600449)");
				will(returnValue(annotationsForKnownUID));

			}

		});

		connection = new DBConnection("localhost", "7050", restFulClient,
				"Default"); //creating the instance of the connection that is being tested by injecting the mocked dependancy

	}

	/**
	 * This test ensures that the determination of Bag existence works as
	 * expected for an existing bag
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testBagExistanceDeterminationWithExistingBag()
			throws RestFulClientException {

		assertTrue(connection.doesBagExist("Favourites"));

	}

	/**
	 * This test ensures that the determination of Bag existence works as
	 * expected for an non existing bag
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testBagExistanceDeterminationWithNotExistingBag()
			throws RestFulClientException {
		assertFalse(connection.doesBagExist("notExisting"));

	}

	/**
	 * Tests whether the reply is negative for an non existing user
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testNonExistingUserReply() throws RestFulClientException {
		assertFalse(connection.doesUserExist("non"));

	}

	/**
	 * Tests whether the reply is positive for an existing user
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testExistingUserReply() throws RestFulClientException {
		assertTrue(connection.doesUserExist("kosta"));
	}

	/**
	 * Tests whether the reply is positive for valid credentials
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testValidCredentials() throws RestFulClientException {
		assertTrue(connection.areCredentialsValid("kosta", "kosta"));
	}

	/**
	 * Tests whether the reply is positive for invalid credentials
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testInValidCredentials() throws RestFulClientException {
		assertFalse(connection.areCredentialsValid("nobody", "really"));
	}

	/**
	 * Given an already known piece of data that the mocked restful client will
	 * return, this test ensures it is parsed correctly into {@link IResultItem}
	 * objects
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testRetreivingFavourites() throws RestFulClientException {
		String expectedFirstTitle = "Fleetwood Mac plan world tour for 2013";
		int expectedSize = 9;
		LinkedList<IResultItem> results = connection
				.retreiveFavourites("kosta");
		System.out.println(results.size());
		assertEquals(expectedSize, results.size());

		assertEquals(expectedFirstTitle, results.get(0).getTitle());

	}

	/**
	 * Given already known data, returned by the restful client(which is
	 * mocked), this test ensures that the expected size of the favorites is
	 * right.
	 * 
	 * @throws RestFulClientException
	 */

	@Test
	public void testRetreivingFavouritesForUserWhoDoesNotHaveFavourites()
			throws RestFulClientException {
		int expectedSize = 0;
		LinkedList<IResultItem> results = connection
				.retreiveFavourites("noone");
		assertEquals(expectedSize, results.size());

	}

	/**
	 * A test that ensures that the retrieval of Annotations work as expected
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testRetreivingAnnotations() throws RestFulClientException {
		int expectedSize = 3;
		LinkedList<IAnnotation> results = connection
				.getAnnotationsForItem("280600449");
		System.out.println(results.size());
		assertEquals(expectedSize, results.size());

	}

}
