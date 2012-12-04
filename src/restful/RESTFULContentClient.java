package restful;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import restful.exceptions.RestFulClientException;

/**
 * 
 * This is a class that contains functions to post and retrieve Json data from
 * RESTFUL interfaces. The class implements {@link IRESTFULClient}.
 * 
 * @author 120010516
 * 
 */
public class RESTFULContentClient implements IRESTFULClient {

	HttpClient client; // the HttpClient that will be used

	/**
	 * Default constructor initialising the {@link HttpClient}
	 */
	public RESTFULContentClient() {

		this.client = new DefaultHttpClient();
	}

	@Override
	public InputStream executeRequest(String address)
			throws RestFulClientException {

		HttpResponse res = null; // the response of the request
		URI httpGetURI; // the URI
		InputStream is = null; // the stream holding the data from the response

		try {
			// executing the request
			httpGetURI = new URI(address);
			HttpGet get = new HttpGet(httpGetURI);
			// getting the response
			res = this.client.execute(get);

			if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				// if there is something wrong with the response, throw an
				// exception
				throw new RestFulClientException("Connection error !");
			}

			// retrieve the content and assign itto the stream that this
			// function returns
			HttpEntity entity = res.getEntity();
			if (entity != null) {
				is = entity.getContent();
			}
		} catch (Exception e) {
			throw new RestFulClientException("Connection error: "
					+ e.getMessage());

		}

		return is;
	}

	@Override
	public void postJson(String adress, String content)
			throws RestFulClientException {

		StringEntity entity; // the entity that will be posted
		HttpPost post = null; // the post request
		try {
			post = new HttpPost(new URI(adress)); // creating the new post
			entity = new StringEntity(content); // creating the entity from the
												// data provided
			entity.setContentType("application/json"); // setting the content
														// type to Json
			post.setEntity(entity);
			// posting
			client.execute(post);
		} catch (Exception e) {
			/*
			 * if problems arise just throw and exception indicating what the
			 * problem with the connection is
			 */
			throw new RestFulClientException("Connection error: "
					+ e.getMessage());
		} finally {
			post.releaseConnection();
		}

	}
}
