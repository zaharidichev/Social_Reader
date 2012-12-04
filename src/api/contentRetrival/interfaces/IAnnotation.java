package api.contentRetrival.interfaces;

/**
 * This is an interface to the implementations of Annotations. Those are objectt
 * holding comments on items that a user has added. An annotation should contain
 * the information for of the user (username), as well as the comment itself.
 * Moreover it should contains the UID of the article.
 * 
 * @author 120010516
 * 
 */
public interface IAnnotation {

	/**
	 * Returns the user that actually created this annotation object
	 * 
	 * @return String username
	 */
	public String getUserName();

	/**
	 * Returns the body of the annotation (the comment of the user)
	 * 
	 * @return {@link String} the annotation itself
	 */
	public String getText();

	/**
	 * Returns the unique ID of the article to which this annotation belongs.
	 * This is useful in finding all the annotations that were created for a
	 * particular article
	 * 
	 * @return {@link String}ring the UID
	 */
	public String getUID();

	public String toString();

}
