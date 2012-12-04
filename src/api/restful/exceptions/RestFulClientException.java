package api.restful.exceptions;

import api.restful.IRESTFULClient;

/**
 * Exception class which purpose is to indicate any problems with
 * {@link IRESTFULClient} implementations
 * 
 * @author 120010516
 * 
 */
public class RestFulClientException extends Exception {

	private static final long serialVersionUID = -3027478613645355642L;

	public RestFulClientException() {
		super();
	}

	public RestFulClientException(String message) {
		super(message);
	}

}
