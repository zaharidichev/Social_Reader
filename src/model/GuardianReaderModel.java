package model;

import java.util.LinkedList;
import java.util.Observable;

import api.contentRetrival.impl.requests.ContentSearchRequest;
import api.contentRetrival.impl.results.factories.ResultSetFactory;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.interfaces.IResultSet;
import api.contentRetrival.interfaces.ISearchRequest;
import api.restful.exceptions.RestFulClientException;

public class GuardianReaderModel extends Observable {

	int pages;
	int currentpage;

	IResultSet results;

	public GuardianReaderModel() throws RestFulClientException {
		ISearchRequest request = new ContentSearchRequest("");
		this.results = ResultSetFactory.getResultSet(request);
		this.setChanged();
		this.notifyObservers();
		this.currentpage = 1;
	}

	public LinkedList<IResultItem> getResults() {
		return this.results.getResultItems();
	}

	public LinkedList<IResultItem> getNextPage() throws RestFulClientException {
		this.results.getNextPage();
		this.setChanged();
		this.notifyObservers();
		return this.getResults();
	}

	public LinkedList<IResultItem> getPrevPage() throws RestFulClientException {
		this.results.getPrevPage();
		this.setChanged();
		this.notifyObservers();
		return this.getResults();
	}

	public void searchForContent(ISearchRequest request) throws RestFulClientException {
		this.results = ResultSetFactory.getResultSet(request);
		this.setChanged();
		this.notifyObservers();
		}

}
