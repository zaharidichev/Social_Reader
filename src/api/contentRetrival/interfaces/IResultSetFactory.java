package api.contentRetrival.interfaces;

import api.restful.exceptions.RestFulClientException;

/**
 * This is an interface to the implementation of factorries that produce items
 * of type {@link IResultSet}. The main purpose of tis interface is to enable
 * the implementation of classes that will abstract away the creation of those
 * objects
 * 
 * @author 120010516
 * 
 */
public interface IResultSetFactory {

	/**
	 * This method returns a new {@link IResultSet} object for a particular
	 * {@link ISearchRequest}. This method handles all the dependency injections
	 * 
	 * @param request
	 *            the {@link ISearchRequest} that needs to be satisfied
	 * @return {@link IResultSet} object
	 * @throws RestFulClientException
	 *             in case something along the restful interfacing goes terribly
	 *             wrong
	 */
	IResultSet getResultSet(ISearchRequest request)
			throws RestFulClientException;

}
