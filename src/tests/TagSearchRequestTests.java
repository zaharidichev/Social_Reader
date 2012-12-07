package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import api.contentRetrival.impl.requests.TagSearchRequest;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ResultType;

/**
 * 
 * This test class contains test that ensure the functionality of
 * {@link ISearchRequest} objects is acting accordingly. The main functionality
 * of the tested class has to do with constructing the appropriate URL string
 * that will query the database.
 * 
 * @author 120010516
 * 
 */
public class TagSearchRequestTests {

	private static TagSearchRequest r;

	@Before
	public void init() {
		r = new TagSearchRequest("facebook");
	}

	/**
	 * Testing basic URL generation
	 */
	@Test
	public void testURLGeneration() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=1&page-size=50&format=json";

		assertEquals(expected, r.getURLAsText());

	}

	/**
	 * Testing whether changes reflect in the URL when the index page is changed
	 */
	@Test
	public void testChangingPageIndex() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=2&page-size=50&format=json";
		r.setPageIndex(2);
		assertEquals(2, r.getPageIndex());
		assertEquals(expected, r.getURLAsText());

	}

	/**
	 * Testing whether the right {@link ResultType} is returned
	 */
	@Test
	public void testResultTypeSet() {
		ResultType expected = ResultType.xml;
		r.setResultType(ResultType.xml);
		assertEquals(expected, r.getResultType());
	}

	/**
	 * Testing thether the generated URL changes according to expectations once
	 * the result type is changed
	 */
	@Test
	public void testProperGenerationOfStringOnChangingResultType() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=1&page-size=50&format=xml";
		r.setResultType(ResultType.xml);

		assertEquals(expected, r.getURLAsText());
	}

}
