package api.contentRetrival.interfaces;

import api.contentRetrival.types.ItemType;

/**
 * Interface to the implementation of result items. Result items are returnes
 * when a {@link ISearchRequest} is being satisfied. Those items contain
 * information about the particularly entity of data being searched.
 * 
 * @author 120010516
 * 
 */
public interface IResultItem {

	/**
	 * Returns the {@link ItemType} of the object. It could be article, tag, etc
	 * 
	 * @return {@link ItemType}
	 */
	public ItemType getType();

	/**
	 * Returns the title of the particular content entity
	 * 
	 * @return {@link String} title
	 */
	public String getTitle();

	/**
	 * Returns the section title in which this piece of information lives
	 * 
	 * @return {@link String} section title
	 */

	public String getSectionTitle();

	/**
	 * Returns the HTTP adress that is the link to this article (Not to the
	 * RestFUL interface, but the WEB one).
	 * 
	 * @return {@link String}
	 */

	public String getLink();

	/**
	 * Returns the unique id of the article or tag. This the String
	 * representation of the hashCode of the title of the piece. This ud is
	 * useful when publishing content relating to this article like annotations,
	 * likes, etc
	 * 
	 * @return {@link String} UID
	 */
	public String getID();

}
