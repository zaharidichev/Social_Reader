package api.restful;

import java.io.InputStream;

import api.restful.exceptions.RestFulClientException;


/**
 * This is an interface to the implementation of clients, interfacing with
 * RESTFUL services. It provides the functionality for executing GET and POST
 * requests
 * 
 * @author 120010516
 * 
 */
public interface IRESTFULClient {

	/**
	 * Executes an HTTP GET request and returns the response
	 * 
	 * @return {@link InputStream} the response of the executed request
	 * @throws RestFulClientException
	 *             in case of connection errors
	 */
	public InputStream executeRequest(String address)
			throws RestFulClientException;

	/**
	 * Executes an http POST to the specified address with the supplied data in
	 * the form of a {@link String}
	 * 
	 * @param adress
	 *            the http address to which the data is being posted
	 * @param content
	 *            a {@link String} containing the structured data
	 * @throws RestFulClientException
	 *             in case any problems arise
	 */
	public void postJson(String adress, String content)
			throws RestFulClientException;

}
