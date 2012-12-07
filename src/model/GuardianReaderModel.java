package model;

import java.util.LinkedList;
import java.util.Observable;

import api.contentRetrival.impl.requests.ContentSearchRequest;
import api.contentRetrival.impl.results.ResultSet;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.IResultSetFactory;
import api.contentRetrival.interfaces.ISearchRequest;
import api.restful.exceptions.RestFulClientException;

/**
 * This class is the model that is the connection between the result set that
 * communicates with the Guardian content API
 * 
 * @author 120010516
 * 
 */
public class GuardianReaderModel extends Observable {

	private IResultSet results; //stores the result set
	private IResultSetFactory factory; // stores the ResultSet Factory

	/**
	 * Public constructor that creates the model and establishes any needed
	 * Connections. Additionally the constructor takes in the factory that is
	 * used for generation {@link ResultSet} objects
	 * 
	 * @throws RestFulClientException
	 */
	public GuardianReaderModel(IResultSetFactory resultSetFactory)
			throws RestFulClientException {
		this.factory = resultSetFactory;
		ISearchRequest request = new ContentSearchRequest(""); // get an empty search request
		this.results = this.factory.getResultSet(request); //get a resultset for this request just to start with something
		//notify observers that the state has changed
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * A public method that retries all the current results
	 * 
	 * @return {@link LinkedList} with {@link IResultItem} obejcts
	 */
	public LinkedList<IResultItem> getResults() {
		return this.results.getResultItems();
	}

	/**
	 * Retrieves the next page of results if any. This is done by direct
	 * communication with the {@link IResultSet} objects
	 * 
	 * @return
	 * @throws RestFulClientException
	 */
	public LinkedList<IResultItem> getNextPage() throws RestFulClientException {
		this.results.getNextPage();
		this.setChanged();
		this.notifyObservers();
		return this.getResults();
	}

	/**
	 * Retrieves the previous page of results if any. This is done by direct
	 * communication with the {@link IResultSet} objects
	 * 
	 * @return
	 * @throws RestFulClientException
	 */
	public LinkedList<IResultItem> getPrevPage() throws RestFulClientException {
		this.results.getPrevPage();
		this.setChanged();
		this.notifyObservers();
		return this.getResults();
	}

	/**
	 * Issues a new {@link ISearchRequest} and obtains a new {@link ResultSet}
	 * that will be used for retriving the new data
	 * 
	 * @param request
	 * @throws RestFulClientException
	 */
	public void searchForContent(ISearchRequest request)
			throws RestFulClientException {
		this.results = this.factory.getResultSet(request);
		//notify observers so interface can be updated accordingly
		this.setChanged();
		this.notifyObservers();
	}

}
