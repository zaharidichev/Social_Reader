package api.contentRetrival.interfaces;

import java.util.LinkedList;

import api.restful.exceptions.RestFulClientException;

/**
 * An interface to the implementation of result sets. Result sets are entities
 * that establish a connection with a database and retrieve information from it.
 * They hold the currently retrieved and processed information. Additionally
 * they provide functionality that allows requesting the preceding or next pages
 * of the result entity being obtained
 * 
 * @author 120010516
 * 
 */
public interface IResultSet {

	/**
	 * Employs the same ideas as the getNextPage() but it updates the currenly
	 * held data with the data from the previous page (if there is a previous
	 * page)
	 * 
	 * @throws RestFulClientException
	 */

	public void getPrevPage() throws RestFulClientException;

	/**
	 * Method that forwards to the next page by issuing a new search request and
	 * updating the currently contained information within the result set with
	 * the new results.
	 * 
	 * @throws RestFulClientException
	 */
	public void getNextPage() throws RestFulClientException;

	/**
	 * Returns the index number of the current page
	 * 
	 * @return {@link Integer}
	 */
	public int getCurrentPage();

	/**
	 * Returns a collection of {@link IResultItem} objects. THose object are the
	 * ones currently held in the buffer of the {@link IResultSet} and
	 * technically correspond to a single page of results
	 * 
	 * @return {@link LinkedList}
	 */
	public LinkedList<IResultItem> getResultItems();

	/**
	 * Based on the header received by the initial request, this method
	 * determines if there are less pages to be showed. That is, if a new
	 * request can be issues with a page lower than the previous one
	 * 
	 * @return {@link Boolean}
	 */
	boolean hasLess();

	/**
	 * This method checks the current page and te avaialable pages and decides
	 * whether there are more pages that can be displayed
	 * 
	 * @return
	 */
	boolean hasMore();

}
