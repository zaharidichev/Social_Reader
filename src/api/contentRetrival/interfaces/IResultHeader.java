package api.contentRetrival.interfaces;

/**
 * An interface to the implementation of a result header. A result header
 * contains the metadata that is contained within a search result. This metadata
 * usually is contained within the header of Json or XML results
 * 
 * @author 120010516
 * 
 */
public interface IResultHeader {

	/**
	 * Retrieves the total number of results
	 * 
	 * @return {@link Integer}
	 */
	public int getNumberOfResults();

	/**
	 * Retrives the number of pages
	 * 
	 * @return {@link Integer}
	 */
	public int getNumberOfPages();

	/**
	 * Retrieves the status of the response
	 *  
	 * @return {@link String}
	 */
	public String getStatus();

}
