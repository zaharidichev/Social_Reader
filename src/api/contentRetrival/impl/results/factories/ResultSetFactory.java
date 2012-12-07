package api.contentRetrival.impl.results.factories;

import api.contentRetrival.impl.results.ResultSet;
import api.contentRetrival.interfaces.IResultItemFactory;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.IResultSetFactory;
import api.contentRetrival.interfaces.ISearchRequest;
import api.restful.IRESTFULClient;
import api.restful.RESTFULContentClient;
import api.restful.exceptions.RestFulClientException;

/**
 * A factory that abstract away the creation of result sets. It is useful for
 * injecting into models (MD pattern)
 * 
 * 
 * @author 120010516
 * 
 */
public class ResultSetFactory implements IResultSetFactory{

	/**
	 * Just an empty default constructor for the result factory
	 */
	public ResultSetFactory() {
	}

	@Override
	public IResultSet getResultSet(ISearchRequest request)
			throws RestFulClientException {
		//handling all the dependency injections here...
		IRESTFULClient client = new RESTFULContentClient();
		IResultItemFactory factory = new ResultItemFactory(client);
		return new ResultSet(request, factory);
	}

}
