package tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import api.contentRetrival.impl.requests.TagSearchRequest;
import api.contentRetrival.types.ResultType;

public class TagSearchRequestTests {

	private static TagSearchRequest r;

	@Before
	public void init() {
		r = new TagSearchRequest("facebook");
	}

	@Test
	public void testURLGeneration() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=1&page-size=50&format=json";

		assertEquals(expected, r.getURLAsText());

	}

	@Test
	public void testChangingPageIndex() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=2&page-size=50&format=json";
		r.setPageIndex(2);
		assertEquals(2, r.getPageIndex());
		assertEquals(expected, r.getURLAsText());

	}

	@Test
	public void testResultTypeSet() {
		ResultType expected = ResultType.xml;
		r.setResultType(ResultType.xml);
		assertEquals(expected, r.getResultType());
	}

	@Test
	public void testProperGenerationOfStringOnChangingResultType() {
		String expected = "http://content.guardianapis.com/tags?q=facebook&page=1&page-size=50&format=xml";
		r.setResultType(ResultType.xml);

		assertEquals(expected, r.getURLAsText());
	}

}
