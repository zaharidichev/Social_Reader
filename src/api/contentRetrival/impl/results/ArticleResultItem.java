package api.contentRetrival.impl.results;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import exceptions.InvalidDateException;

import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.types.ItemType;

/**
 * An implementation of {@link IResultItem}. this class is used to create
 * objects that contain the data needed for expressing the concept of an Article
 * object. It defers from a Tag in that it holds additional information such as
 * the image in raw byte[] format, author, date, etc
 * 
 * @author 120010516
 * 
 */
public class ArticleResultItem implements IResultItem {

	private String title; // the title of the article
	private String sectionTitle; //the title of the section
	private String id; //the id of the article
	private String date; // the date on which the article was published
	private String webLink; // the link to the article
	private byte[] thumbnail; // the thumbnail of the article as a byte[]
	private String author; // the author of the article

	/**
	 * The constructor used to create an instance of this class
	 * 
	 * @param title
	 *            the title of the article
	 * @param section
	 *            the title of the section
	 * @param id
	 *            the id of the article
	 * @param date
	 *            the date on which the article was published
	 * @param link
	 *            the link to the article
	 * @param author
	 *            the author of the article
	 * @param image
	 *            the thumbnail of the article as a byte[]
	 */
	public ArticleResultItem(String title, String section, String id,
			String date, String link, String author, byte[] image) {
		this.title = title;
		this.sectionTitle = section;
		this.id = id;
		this.date = date;
		this.webLink = link;
		this.thumbnail = image;
		this.author = author;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Title: " + this.title + " " + "Author: " + this.author);
		return sb.toString();
	}

	@Override
	public ItemType getType() {
		return ItemType.content;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getSectionTitle() {
		return this.sectionTitle;
	}

	@Override
	public String getID() {
		return Integer.toString(this.id.hashCode());
	}

	@Override
	public String getLink() {
		return this.webLink;
	}

	/**
	 * Returns a {@link Date} object that indicated when the article was
	 * published
	 * 
	 * @return a {@link Date} object
	 * @throws InvalidDateException
	 *             in case the date cannot be parsed
	 */
	public Date getDate() throws InvalidDateException {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		try {
			date = formatter.parse(this.date);
		} catch (ParseException e) {
			throw new InvalidDateException("Invalid date of article");
		}
		return date;
	}

	/**
	 * Returns the date of the object in a text format
	 * 
	 * @return {@link String} the date
	 */
	public String getDateAsText() {
		return this.date;
	}

	/**
	 * Returns the raw bytes of the thumbnail
	 * 
	 * @return byte[]
	 */
	public byte[] getThumbnail() {
		return this.thumbnail;
	}

	/**
	 * returns the name ofthe author of the article
	 * 
	 * @return {@link String}
	 */
	public String getAuthor() {
		return this.author;
	}

}
