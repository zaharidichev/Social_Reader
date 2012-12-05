package api.contentRetrival.impl.results.factories;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import utils.WebUtils;
import api.contentRetrival.impl.results.ArticleResultItem;
import api.contentRetrival.impl.results.ResultHeader;
import api.contentRetrival.impl.results.TagResultItem;
import api.contentRetrival.interfaces.IResultHeader;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.restful.IRESTFULClient;
import api.restful.RESTFULContentClient;
import api.restful.exceptions.RestFulClientException;

/**
 * A factory class that abstract away the production of {@link IResultItem} and
 * {@link IResultHeader} objects. The main functionality of the factory is to
 * issue {@link ISearchRequest} to the {@link RESTFULContentClient} static class
 * and turn the results into collections of {@link IResultItem} and
 * {@link IResultHeader} objects. This involves the parsing of Json objects
 * 
 * @author 120010516
 * 
 */
public class ResultItemFactory implements IResultItemFactory {

	private IRESTFULClient client; // the restful client that will be used for retreiving data

	/**
	 * Constructor that inject the dependency of the {@link IRESTFULClient}
	 * object
	 * 
	 * @param client
	 */
	public ResultItemFactory(IRESTFULClient client) {
		this.client = client;

	}

	/*
	 * This convinience method is responsible for parsing a Json node and
	 * producing a collection of actual result items
	 */
	private LinkedList<IResultItem> parseItems(JsonNode root, ItemType type) {
		LinkedList<IResultItem> results = new LinkedList<IResultItem>(); //will hold the results

		for (JsonNode item : root.path("response").path("results")) {

			// for all of the entities that are being returned for this search

			String id = item.path("id").getTextValue(); //get the id
			String title = item.path("webTitle").getTextValue(); // get the title
			String section = item.path("sectionName").getTextValue(); //get the section
			String url = item.path("webUrl").getTextValue(); // get the URL
			if (type == ItemType.tag) {
				//if it is a tag
				IResultItem newTagItem = new TagResultItem(id, title, section,
						url);
				results.add(newTagItem); //add a new tag to the list of results
			} else {
				// if it is an article get some more data

				String date = item.path("webPublicationDate").getTextValue()
						.split("T")[0]; //get the date it was published

				String author; //get the author
				try {
					// try to get the author
					author = item.path("fields").get("byline").getTextValue();
				} catch (NullPointerException e) {
					// if null, assume author unknown
					author = "Unknown";
				}

				byte[] thumbnail = new byte[0]; // will hold the raw bytes for the picture

				try {
					String thumbnailLink = item.path("fields").get("thumbnail")
							.getTextValue();
					thumbnail = WebUtils.getImageFromWeb(thumbnailLink);

				} catch (NullPointerException e) {
					//just do nothing.. we already have an empty byte array, that should be sufficient
				} catch (IOException e) {
					//just carry on, the system should be resilient to faulty data
				}

				ArticleResultItem newItem = new ArticleResultItem(title,
						section, id, date, url, author, thumbnail);
				results.add(newItem); // create a new article and add it to the resutls
			}

		}

		return results;
	}

	@Override
	public IResultHeader makeHeader(ISearchRequest request)
			throws RestFulClientException {

		InputStream is = this.client.executeRequest(request.getURLAsText()); // executes a request and gets back the response as an IS
		JsonNode root = null;
		try {
			root = WebUtils.parseJson(is); //trying to parse the result into Json
		} catch (Exception e) {
			//in case the parsing did not go so well, assume something went wrong with the connection to the resful service
			throw new RestFulClientException("Invalid Json object  retreived");
		}

		//Retrieving all the fields needed for the construction of a header
		String status = root.path("response").get("status").getTextValue();
		int pages = root.path("response").get("pages").getIntValue();
		int results = root.path("response").get("total").getIntValue();
		//Returning the newly constructed header
		return new ResultHeader(results, pages, status);
	}

	@Override
	public LinkedList<IResultItem> makeResultItems(ISearchRequest request)
			throws RestFulClientException {

		// pre  initialising
		InputStream is = null;
		JsonNode root = null;
		try {
			// getting the raw results as a stream
			is = this.client.executeRequest(request.getURLAsText());
			//parsing into a json node
			root = WebUtils.parseJson(is);

			// Laundering the occurred exceptions
		} catch (JsonProcessingException e) {
			throw new RestFulClientException("Invalid Json");
		} catch (IOException e) {
			throw new RestFulClientException("I/O problem");

		}

		//returns the result of parsing the Json tree
		return this.parseItems(root, request.getType());

	}
}
