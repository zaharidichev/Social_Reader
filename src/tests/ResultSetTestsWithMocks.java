package tests;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import api.contentRetrival.impl.results.ResultHeader;
import api.contentRetrival.impl.results.ResultSet;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.contentRetrival.types.ResultType;
import api.restful.exceptions.RestFulClientException;

/**
 * A class that contains tests, ensuring that the methods in {@link ResultSet}
 * objects work as expected. For the purpose several mocks are created to
 * satisfy the dependency injection
 * 
 * @author 120010516
 * 
 */
public class ResultSetTestsWithMocks {

	private final Mockery context = new JUnit4Mockery(); // the mockery
	private IResultSet resultSet;

	private ISearchRequest mockRequest; //the mock request
	private IResultItemFactory mockResultFactory; // the mock result factory 
	private IResultItem mockTagResult; // the mock tag result

	/*
	 * method to create all the mocked functionality
	 */
	private void createMocks() throws RestFulClientException {
		mockRequest = context.mock(ISearchRequest.class, "mockRequest");
		mockTagResult = context.mock(IResultItem.class, "mockTagResult");
		mockResultFactory = context.mock(IResultItemFactory.class,
				"mockResultFactory");

		context.checking(new Expectations() {
			{
				LinkedList<IResultItem> mockResultList = new LinkedList<IResultItem>();
				mockResultList.add(mockTagResult);

				// mocking needed methods
				allowing(mockRequest).getPageIndex();
				will(returnValue(1));

				allowing(mockRequest).getURLAsText();
				will(returnValue("http://URL"));

				allowing(mockRequest).getResultType();
				will(returnValue(ResultType.json));

				allowing(mockRequest).getType();
				will(returnValue(ItemType.tag));

				allowing(mockTagResult).getID();
				will(returnValue("382"));

				allowing(mockTagResult).getLink();
				will(returnValue("http://link"));

				allowing(mockTagResult).getSectionTitle();
				will(returnValue("Section Title"));

				allowing(mockTagResult).getTitle();
				will(returnValue("Title"));

				allowing(mockTagResult).getType();
				will(returnValue(ItemType.tag));

				allowing(mockResultFactory).makeResultItems(mockRequest);
				will(returnValue(mockResultList));

				allowing(mockResultFactory).makeHeader(mockRequest);
				will(returnValue(new ResultHeader(1, 1, "ok")));

			}
		});

	}

	/**
	 * Executed before every test in order to provide us with fresg mocked
	 * objects
	 * 
	 * @throws RestFulClientException
	 */
	@Before
	public void init() throws RestFulClientException {
		createMocks();
		resultSet = new ResultSet(mockRequest, mockResultFactory);
	}

	/**
	 * Tests whether the expected result item is returned appropriately
	 */
	@Test
	public void testRetreivingResults() {
		IResultItem item = resultSet.getResultItems().get(0);
		assertEquals(mockTagResult.getID(), item.getID());
		assertEquals(mockTagResult.getLink(), item.getLink());
		assertEquals(mockTagResult.getSectionTitle(), item.getSectionTitle());
		assertEquals(mockTagResult.getType(), item.getType());
		assertEquals(mockTagResult.getTitle(), item.getTitle());

	}

	/**
	 * Tests the incremnetation of a page. In this case nothing should be
	 * incremented since there are no more pages
	 */
	@Test
	public void testIncrementingPage() throws RestFulClientException {
		int before = resultSet.getCurrentPage();
		resultSet.getNextPage();
		assertEquals(before, resultSet.getCurrentPage());
	}

	/**
	 * Tests the retreival of the previous page. Again in this case nothing
	 * should change since there are no previous pages. This is critical
	 * functionality that overcomes the problem of reaching either ends of a
	 * piece of Json data
	 * 
	 * @throws RestFulClientException
	 */
	@Test
	public void testDecrementingPage() throws RestFulClientException {
		int before = resultSet.getCurrentPage();
		resultSet.getPrevPage();
		assertEquals(before, resultSet.getCurrentPage());
	}

}
