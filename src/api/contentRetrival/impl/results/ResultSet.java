package api.contentRetrival.impl.results;

import java.util.LinkedList;

import api.contentRetrival.interfaces.IResultHeader;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.ISearchRequest;
import api.restful.exceptions.RestFulClientException;

/**
 * An implementation of the {@link IResultSet} interface, this class provides
 * functionality that enables its clients to work with a piece of data retreived
 * by satisfying a {@link ISearchRequest} object.
 * 
 * @author 120010516
 * 
 */
public class ResultSet implements IResultSet {

	private ISearchRequest request; // holds the request
	private IResultHeader header; //holds the header
	private LinkedList<IResultItem> items; //holds the items 
	private int currentPage; // stores the index of the current page
	private IResultItemFactory factory; //stores the factory needed to produce result items

	/**
	 * The constructor used to create an instance of this class
	 * 
	 * @param request
	 *            the request that needs to be satisfied
	 * @param factory
	 *            the factory that will be used for producing
	 *            {@link IResultItem} objects
	 * @throws RestFulClientException
	 */
	public ResultSet(ISearchRequest request, IResultItemFactory factory)
			throws RestFulClientException {

		// initialising variables
		this.factory = factory;
		this.header = factory.makeHeader(request);

		this.request = request;
		this.currentPage = 1; //setting current page to 1 
		this.updateResults();
	}

	@Override
	public void getNextPage() throws RestFulClientException {
		if (this.hasMore()) {
			// only if there are more
			this.currentPage++; //increment page index
			//set the page of the request
			this.request.setPageIndex(this.currentPage);
			this.updateResults(); //update the resutls

		}

	}

	@Override
	public void getPrevPage() throws RestFulClientException {
		if (this.hasLess()) {
			//only if there are less
			this.currentPage--; // decrement
			//set request to correct page
			this.request.setPageIndex(this.currentPage);
			this.updateResults(); //update content
		}

	}

	@Override
	public LinkedList<IResultItem> getResultItems() {
		return this.items;
	}

	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}

	@Override
	public boolean hasLess() {
		if ((this.currentPage - 1) < 1)
			return false;
		return true;
	}

	@Override
	public boolean hasMore() {
		if ((this.currentPage + 1) > this.header.getNumberOfPages())
			return false;
		return true;
	}

	/*
	 * reissues the request (which is probably updated) to retreive the new data
	 */
	private void updateResults() throws RestFulClientException {

		this.items = factory.makeResultItems(this.request);

	}
}
