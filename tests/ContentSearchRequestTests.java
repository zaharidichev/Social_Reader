import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import api.contentRetrival.impl.requests.ContentSearchRequest;
import api.contentRetrival.impl.requests.TagSearchRequest;
import api.contentRetrival.types.ResultType;

public class ContentSearchRequestTests {
	private static ContentSearchRequest r;

	@Before
	public void init() {
		r = new ContentSearchRequest("facebook");
	}

	@Test
	public void testURLGeneration() {
		String expected = "http://content.guardianapis.com/search?q=facebook&page=1&format=json&page-size=50&show-fields=all";
		System.out.println(r.getURLAsText());
		assertEquals(expected, r.getURLAsText());

	}

	@Test
	public void testSettingSectionFilter() {
		r.setSectionFilter("tech");
		String expected = "http://content.guardianapis.com/search?q=facebook&page=1&section=tech&format=json&page-size=50&show-fields=all";
		assertEquals(expected, r.getURLAsText());

	}

	@Test
	public void testSettingTagFilter() {
		r.setTagFilter("tech");
		String expected = "http://content.guardianapis.com/search?q=facebook&page=1&tag=tech&format=json&page-size=50&show-fields=all";
		assertEquals(expected, r.getURLAsText());

	}

	@Test
	public void testSettingTagAndSectionFilter() {
		r.setTagFilter("tech");
		r.setSectionFilter("tech2");

		String expected = "http://content.guardianapis.com/search?q=facebook&page=1&tag=tech&section=tech2&format=json&page-size=50&show-fields=all";
		assertEquals(expected, r.getURLAsText());

	}
	
	@Test
	public void testChangingPageIndex() {
		r.setPageIndex(10);
		assertEquals(10, r.getPageIndex());
		String expected = "http://content.guardianapis.com/search?q=facebook&page=10&format=json&page-size=50&show-fields=all";
		assertEquals(expected, r.getURLAsText());

	}
}
