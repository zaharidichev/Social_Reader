package api.contentRetrival.impl.requests;

import utils.Settings;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.contentRetrival.types.ResultType;

/**
 * This class is an implementation of {@link ISearchRequest}. It is used to
 * request the retrieval of article results. It provides the functionality for
 * specifying a particular keyword and additional criteria like section and tag.
 * Based on those criteria a request URL query is assembled
 * 
 * @author 120010516
 * 
 */
public class ContentSearchRequest implements ISearchRequest {



	private String requestAddress; // the generated search quarry that is going to be used to query the RESTFUL service
	private String keyword; //the keyword of the search
	private String sectionFilter; // a filter for sections
	private String tagFilter; //a filter for tags
	private ResultType resultType; //the result type (json, XML, etc.)
	private int pageIndex; // the page index being requested

	/**
	 * Constructor that takes only a keyword without any filters
	 * 
	 * @param keyword
	 *            {@link String} the keyword search criteria
	 */
	public ContentSearchRequest(String keyword) {
		// intialising variables
		this.keyword = keyword;
		this.sectionFilter = "";
		this.tagFilter = "";
		this.resultType = Settings.getResultType();
		this.pageIndex = 1;
		// assmbling the URL for the query
		assambleSearchURL();
	}

	//A constructor allowing the specification of additional tag and section filters on creation
	public ContentSearchRequest(String keyword, String section, String tag) {
		//again initialising variables 
		this.keyword = keyword;
		this.sectionFilter = section;
		this.tagFilter = tag;
		this.resultType = Settings.getResultType();
		this.pageIndex = 1;
		// and constructing the the actual URL string
		assambleSearchURL();

	}

	/**
	 * A method that allows specifying a new keyword
	 * 
	 * @param keyword
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
		//Reassembling the URL so the new keyword is reflected
		assambleSearchURL();

	}

	/**
	 * A method that allows specifying a tag filter
	 * 
	 * @param tag
	 */
	public void setTagFilter(String tag) {
		this.tagFilter = tag;
		assambleSearchURL(); //reassembling again

	}

	/**
	 * A method that allows specifying a section filter
	 * 
	 * @param section
	 */
	public void setSectionFilter(String section) {
		this.sectionFilter = section;
		assambleSearchURL(); //reassembling again

	}

	/**
	 * Allows the specification of a particular index Page
	 */
	public void setPageIndex(int index) {
		this.pageIndex = index;
		assambleSearchURL();
	}

	/**
	 * Retrieves the current index page
	 */
	public int getPageIndex() {
		return this.pageIndex;
	}

	/**
	 * This is a convinience method which takes the current values of the
	 * private variables and assembles a url accordingly
	 */
	private void assambleSearchURL() {
		StringBuilder url = new StringBuilder();
		url.append(Settings.getAPIAddress()); // appends the IP 
		url.append("search?q=" + this.keyword); // appends the keyword
		url.append("&page=" + this.pageIndex); //appends the page index

		if (this.tagFilter.length() > 0) {
			url.append("&tag=" + this.tagFilter); // if there is a tag filter specified, puts that in
		}
		if (this.sectionFilter.length() > 0) {
			url.append("&section=" + this.sectionFilter); //if there is a section filter ,that is added too
		}
		url.append("&format=" + this.resultType
				+ "&page-size=50&show-fields=all"); // sets the format of the request

		//finally the newly assembled string is assigned to  the private variable holding the adress
		this.requestAddress = url.toString();
	}

	@Override
	public ItemType getType() {
		return ItemType.content;
	}

	@Override
	public String getURLAsText() {
		return this.requestAddress;
	}

	@Override
	public ResultType getResultType() {
		return this.resultType;
	}

	@Override
	public void setResultType(ResultType type) {
		this.resultType = type;
		this.assambleSearchURL();
	}

	@Override
	public String toString() {
		return this.getURLAsText();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContentSearchRequest other = (ContentSearchRequest) obj;
		if (keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!keyword.equals(other.keyword))
			return false;
		return true;
	}

}
