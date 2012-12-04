package api.contentRetrival.interfaces;

import java.util.LinkedList;

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
	 */

	public void getPrevPage();

	/**
	 * Method that forwards to the next page by issuing a new search request and
	 * updating the currently contained information within the result set with
	 * the new results.
	 */
	public void getNextPage();

	/**
	 * Returns the index number of the current page
	 * 
	 * @return {@link Integer}
	 */
	public int getCurrentPage();

	/**
	 * Returns a collection of {@link IResultItem} objects. THose object are the
	 * ones currently held in the buffer od the {@link IResultSet} and
	 * technically correspond to a single page of results
	 * 
	 * @return
	 */
	public LinkedList<IResultItem> getResultItems();

}
