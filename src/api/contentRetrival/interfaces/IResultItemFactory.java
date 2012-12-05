package api.contentRetrival.interfaces;

import java.util.LinkedList;

import api.restful.IRESTFULClient;
import api.restful.exceptions.RestFulClientException;

/**
 * An interface for a factory that abstract away the creation and retrival of
 * results given search requests
 * 
 * @author 120010516
 * 
 */
public interface IResultItemFactory {
	/**
	 * Given an {@link ISearchRequest} this method produces an
	 * {@link IResultHeader} by issuing a request to the restful client
	 * 
	 * @param request
	 *            the {@link ISearchRequest}
	 * @return the {@link IResultHeader} object containing the data for the
	 *         response to the provided {@link ISearchRequest}
	 * @throws RestFulClientException
	 *             in case there were any problems with the connection to the
	 *             restful interface
	 */
	public IResultHeader makeHeader(ISearchRequest request)
			throws RestFulClientException;

	/**
	 * A method that produces collection of {@link IResultItem} objects for a
	 * particular {@link ISearchRequest}. The method facilitates a connection to
	 * the {@link IRESTFULClient} and retrieves the data, then parses it to Json
	 * and finally parses the Json to produce {@link IResultItem} instances
	 * 
	 * @param request
	 *            the {@link ISearchRequest} that needs to be satisfied
	 * @return the {@link IResultItem} objects in a {@link LinkedList}
	 * @throws RestFulClientException
	 *             in case there are problems with the connection and retrieval
	 *             of data from the {@link IRESTFULClient} object
	 */
	public LinkedList<IResultItem> makeResultItems(ISearchRequest request)
			throws RestFulClientException;

}
