package api.contentRetrival.impl.results;

import api.contentRetrival.interfaces.IResultHeader;

public class ResultHeader implements IResultHeader {

	private int numberOfResults; // holds the number of results
	private int numberOfPages; // holds the number of pages
	private String status; //holds the status

	/**
	 * A constructor requuiring all the needed values to initialise the member
	 * variables
	 * 
	 * @param results
	 * @param pages
	 * @param status
	 */
	public ResultHeader(int results, int pages, String status) {
		this.numberOfResults = results;
		this.numberOfPages = pages;
		this.status = status;
	}

	public int getNumberOfResults() {
		return this.numberOfResults;
	}

	public int getNumberOfPages() {
		return this.numberOfPages;
	}

	public String getStatus() {
		return this.status;
	}

}
