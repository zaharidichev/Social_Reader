package api.contentRetrival.interfaces;

import api.contentRetrival.types.ItemType;
import api.contentRetrival.types.ResultType;

/**
 * An interface to the implementation of a request to be issued for a particular
 * content retrieval system. It holds important information from which the
 * string used to query a restful service can be derived
 * 
 * @author 120010516
 * 
 */
public interface ISearchRequest {

	/**
	 * Used to obtain the {@link ItemType} of the request (whether it is an
	 * article or tag or anything else)
	 * 
	 * @return {@link ItemType}
	 */
	public ItemType getType();

	/**
	 * Returns the HTTP address that is used to query the RESTfUL service
	 * 
	 * @return {@link String}
	 */

	public String getURLAsText();

	/**
	 * Returns a {@link ResultType} object indicating what is the type of data
	 * being requested
	 * 
	 * @return {@link ResultType}
	 */
	public ResultType getResultType();

	/**
	 * Changes the type of data being rquested, thus forcing the querry adress
	 * to be regenerated
	 * 
	 * @param type
	 *            {@link ResultType} the type of data
	 */
	public void setResultType(ResultType type);

	/**
	 * Sets the page index of the result. This is used since with RESTful not
	 * all the results are returned at once. The request needs to specify how
	 * many results per page and which page to request
	 * 
	 * @param index
	 */
	public void setPageIndex(int index);

	/**
	 * Retrieves the current page index of the request
	 * 
	 * @return {@link Integer}
	 */
	public int getPageIndex();
}
