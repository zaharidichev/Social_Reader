package api.contentRetrival.impl.requests;

import utils.Settings;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.contentRetrival.types.ResultType;

/**
 * Much like {@link ContentSearchRequest}, this class is responsible for coockie
 * cutting objects that contain all the information needed to issue a tag search
 * 
 * @author 120010516
 * 
 */
public class TagSearchRequest implements ISearchRequest {

	private String requestAddress; // the HTTP query
	private String keyword; // the keyword 
	private ResultType resultType; // the result type
	private int pageIndex; //the current page index

	/**
	 * A constructor that takes only the keyword needed for the search of a tag.
	 * Additional filters are not implemented since a TAG is by itself a filter
	 * 
	 * @param keyword
	 */
	public TagSearchRequest(String keyword) {
		//assigning variables
		this.keyword = keyword;
		this.pageIndex = 1;
		this.resultType = Settings.getResultType();
		// assambling the String 
		this.assambleSearchURL();
	}

	@Override
	public ItemType getType() {
		return ItemType.tag;
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
	public void setPageIndex(int index) {
		this.pageIndex = index;
		this.assambleSearchURL();
	}

	@Override
	public int getPageIndex() {
		return this.pageIndex;
	}

	/**
	 * A convenience method that assembles the URL needed for issuing a search
	 * request
	 */
	private void assambleSearchURL() {
		StringBuilder url = new StringBuilder();
		url.append(Settings.getAPIAddress());
		url.append("tags?q=" + this.keyword);
		url.append("&page=" + this.pageIndex);

		url.append("&page-size=50&format=" + this.resultType);
		this.requestAddress = url.toString();
	}

}
