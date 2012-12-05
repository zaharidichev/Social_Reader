package unittests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import utils.Utils;
import api.contentRetrival.impl.results.ArticleResultItem;
import api.contentRetrival.impl.results.TagResultItem;
import api.contentRetrival.impl.results.factories.ResultItemFactory;
import api.contentRetrival.interfaces.IResultHeader;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.contentRetrival.types.ResultType;
import api.restful.IRESTFULClient;
import api.restful.exceptions.RestFulClientException;

/**
 * This class contains test that ensure the functionality of
 * {@link ResultItemFactory} works ass expected. For the purpose mocks of
 * {@link InputStream} data objects are created forom external text files.
 * Additionally mock os {@link ISearchRequest} are used along with mocks of
 * {@link IRESTFULClient}
 * 
 * @author 120010516
 * 
 */
@RunWith(JMock.class)
public class ResultItemFactoryTestswithMocking {

	private final Mockery context = new JUnit4Mockery(); // the mockery



	private IRESTFULClient mockClient; // the mock restful client
	private InputStream mockDataResult; // the mock response data that is read from disk
	private IResultItemFactory resultFactory; // the result factory being tested
	private ISearchRequest mockArticleRequest; // the mock article request
	private ISearchRequest mockTagRequest; // the mock tag request

	private String facebookRequestString = "http://content.guardianapis.com/search?q=facebook&page=1&format=json&page-size=50&show-fields=all";
	private String facbookTagString = "http://content.guardianapis.com/tags?q=facebook&format=json";

	private String article = "resources/mockArtResp.txt"; // file containing data for an article
	private String tag = "resources/mockTagResp.txt"; // file containing data for a tag result

	/*
	 * the method creates mock objects as needed, provided a String that
	 * indicated the path to the file containing mock data
	 */
	public void createMocks(String mockData) throws RestFulClientException,
			IOException {

		mockDataResult = new ByteArrayInputStream(Utils.getMockResponse(mockData));
		mockClient = context.mock(IRESTFULClient.class); // the mock tag requestked client
		mockArticleRequest = context.mock(ISearchRequest.class,
				"mockArticleRequest"); //the mock article request
		mockTagRequest = context.mock(ISearchRequest.class, "mockTagRequest"); // the moc

		context.checking(new Expectations() {
			{

				// mocking needed methods
				allowing(mockClient).executeRequest(facebookRequestString);
				will(returnValue(mockDataResult));

				allowing(mockClient).executeRequest(facbookTagString);
				will(returnValue(mockDataResult));

				allowing(mockArticleRequest).getPageIndex();
				will(returnValue(1));
				allowing(mockArticleRequest).getResultType();
				will(returnValue(ResultType.json));
				allowing(mockArticleRequest).getType();
				will(returnValue(ItemType.content));
				allowing(mockArticleRequest).getURLAsText();
				will(returnValue(facebookRequestString));

				allowing(mockTagRequest).getPageIndex();
				will(returnValue(1));
				allowing(mockTagRequest).getResultType();
				will(returnValue(ResultType.json));
				allowing(mockTagRequest).getType();
				will(returnValue(ItemType.tag));
				allowing(mockTagRequest).getURLAsText();
				will(returnValue(facbookTagString));

			}
		});

		resultFactory = new ResultItemFactory(this.mockClient);

	}

	/**
	 * This test ensures that the process of creating an array a colelction of
	 * {@link ArticleResultItem} objects works as expected, provided valid data
	 * 
	 * @throws RestFulClientException
	 * @throws IOException
	 */
	@Test
	public void testMakingAnArticleItemsList() throws RestFulClientException,
			IOException {
		createMocks(article);
		ArticleResultItem item = (ArticleResultItem) resultFactory
				.makeResultItems(mockArticleRequest).get(0);

		assertEquals("Richard Adams in Washington DC", item.getAuthor());
		assertEquals(
				"Fiscal cliff: Obama meets governors as GOP offer faces sharp scrutiny – live",
				item.getTitle());
		assertEquals("World news", item.getSectionTitle());

	}

	/**
	 * This method ensures that the process of making a header works as expected
	 * 
	 * @throws RestFulClientException
	 * @throws IOException
	 */
	@Test
	public void testMakingAHeader() throws RestFulClientException, IOException {
		createMocks(article);

		IResultHeader header = resultFactory.makeHeader(mockArticleRequest);
		assertEquals(24378, header.getNumberOfPages());
		assertEquals(24378, header.getNumberOfResults());
		assertEquals("ok", header.getStatus());

	}

	/**
	 * Ensures that the process of creating {@link TagResultItem} objects is
	 * working as expected
	 * 
	 * @throws RestFulClientException
	 * @throws IOException
	 */
	@Test
	public void testMakingATag() throws RestFulClientException, IOException {
		createMocks(tag);

		TagResultItem tag = (TagResultItem) resultFactory.makeResultItems(
				mockTagRequest).get(0);

		assertEquals("Facebook", tag.getTitle());
		assertEquals("Technology", tag.getSectionTitle());

	}

}
