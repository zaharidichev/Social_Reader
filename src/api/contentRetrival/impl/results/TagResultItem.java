package api.contentRetrival.impl.results;

import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.types.ItemType;

/**
 * An implementation of the {@link IResultItem} interface. An instance of this
 * class is an object that encapsulates all the needed information to present a
 * tag item
 * 
 * @author 120010516
 * 
 */
public class TagResultItem implements IResultItem {

	String title; // the title of the tag
	String url; //the url to this tag
	String section; //the section with which this tag is associated
	String id; //the id of the tag

	/**
	 * A constructor taking all the needed values to construct the object
	 * 
	 * @param id
	 *            the id of the tag
	 * @param title
	 *            the title of the tag
	 * @param section
	 *            the section with which this tag is associated
	 * @param url
	 *            the url to this tag
	 */
	public TagResultItem(String id, String title, String section, String url) {
		this.id = id;
		this.title = title;
		this.section = section;
		this.url = url;

	}

	@Override
	public ItemType getType() {
		return ItemType.tag;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getSectionTitle() {
		return this.section;
	}

	@Override
	public String getID() {
		return Integer.toString(this.id.hashCode());
	}

	@Override
	public String getLink() {
		return this.url;
	}

	@Override
	public String toString() {

		return "Title: " + this.title + " Section: " + this.section;

	}

}
