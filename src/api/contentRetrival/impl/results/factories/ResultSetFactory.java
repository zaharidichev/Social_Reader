package api.contentRetrival.impl.results.factories;

import api.contentRetrival.impl.results.ResultSet;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.ISearchRequest;
import api.restful.IRESTFULClient;
import api.restful.RESTFULContentClient;
import api.restful.exceptions.RestFulClientException;

/**
 * A static factory that abstract away the creation of result sets
 * 
 * @author 120010516
 * 
 */
public class ResultSetFactory {

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
	public static IResultSet getResultSet(ISearchRequest request)
			throws RestFulClientException {
		//handling all the dependency injections here...
		IRESTFULClient client = new RESTFULContentClient();
		IResultItemFactory factory = new ResultItemFactory(client);
		return new ResultSet(request, factory);
	}

}
