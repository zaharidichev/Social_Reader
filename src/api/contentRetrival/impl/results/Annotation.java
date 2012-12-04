package api.contentRetrival.impl.results;

import api.contentRetrival.interfaces.IAnnotation;

/**
 * An implementation of the {@link IAnnotation} interface, this class
 * encapsulates all the information needed to express an Annotation concept.
 * 
 * @author 120010516
 * 
 */
public class Annotation implements IAnnotation {

	private String userName; //the user name
	private String text;// the text of the annotation itself
	private String UID; // the unique ID of the annotated item

	/**
	 * Constructor requiring parameters in order to initialis the username,
	 * unique item ID and the annotation text itself
	 * 
	 * @param userName
	 * @param text
	 * @param itemUID
	 */
	public Annotation(String userName, String text, String itemUID) {
		//initialising variables
		this.userName = userName;
		this.text = text;
		this.UID = itemUID;
	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getUID() {
		return this.UID;
	}

	@Override
	public String toString() {

		return this.userName + " says: " + this.text;
	}

}
