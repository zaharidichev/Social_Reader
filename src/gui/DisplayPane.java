package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import api.contentRetrival.impl.results.ArticleResultItem;
import api.contentRetrival.impl.results.TagResultItem;
import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.types.ItemType;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.Preference;
import api.userDataPersistance.PreferenceType;
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * This class takes care of providing functionality to display
 * {@link IResultItem} objects. It contains methods for building the text area
 * and inserting thumbnails if any. Additionally it contains buttons for
 * annotating liking, disliking and bookmarking the particular item
 * 
 * @author 120010516
 * 
 */
public class DisplayPane extends JPanel {

	private static final long serialVersionUID = -5710893582352964181L;

	private JToolBar tools; // the toolbar with the action buttons
	private JTextArea itemInfo; // this area displays all the textual info about the item
	private JScrollPane infoScroll; // this is the scroll pane that contains the text area
	private IDBConnection connection; // an instance of a database connection for persisting user actions such as annotations

	private IResultItem currentItem; // the current item being displayed

	private JButton like; //like button
	private JButton dislike; //dislike button

	private JButton annotate; //annotate button
	private JButton bookmark; // bookmark button
	private String currentUser;

	/**
	 * A constructor that takes in the {@link IDBConnection} object that will be
	 * used for persisting user actions
	 * 
	 * @param connection
	 */
	public DisplayPane(IDBConnection connection) {

		this.connection = connection;
		this.currentUser = this.connection.getLoggedUser(); //retrives the currently logged user

		this.constructTools(); //constructs the toolbar

		//adding components and setting sizes
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.setPreferredSize(new Dimension(520, 300));

		itemInfo = new JTextArea();
		this.infoScroll = new JScrollPane(this.itemInfo,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		infoScroll.setSize(new Dimension(500, 300));

		this.add(infoScroll);
		this.setPreferredSize(new Dimension(520, 300));
		this.addListeners();
		this.updateUI();

	}

	/**
	 * Given an {@link ArticleResultItem} this method is used to construct the
	 * text string that is displayed in the text area providing details for the
	 * article
	 * 
	 * @param item
	 */
	private void renderArticleInfo(ArticleResultItem item) {

		// trying to get the  thumbnail if any
		if (item.getThumbnail().length > 0) {
			System.out.println(item.getThumbnail()[7]);
			InputStream in = new ByteArrayInputStream(item.getThumbnail());
			BufferedImage thumb;
			Component picLabel = null;

			try {
				thumb = ImageIO.read(in);
				picLabel = new JLabel(new ImageIcon(thumb));

			} catch (IOException e) {
				new InfoForUser(e.getMessage());
			}
			this.add(picLabel, BorderLayout.PAGE_START);
			this.updateUI();
		}
		//building all the content 
		StringBuilder details = new StringBuilder();
		details.append("Title: " + item.getTitle() + "\n");
		details.append("Author: " + item.getAuthor() + "\n");
		try {
			details.append("Dislikes: "
					+ this.connection.getNumberOfPreferences(item.getID(),
							PreferenceType.dislike) + "\n");

			details.append("Likes: "
					+ this.connection.getNumberOfPreferences(item.getID(),
							PreferenceType.like) + "\n");
		} catch (RestFulClientException e) {
			new InfoForUser(e.getMessage());

		}

		details.append("Section: " + item.getSectionTitle() + "\n");
		details.append("URL: " + item.getLink() + "\n");

		this.itemInfo.setText(details.toString());

	}

	/**
	 * This method constructs the string needed for displaying a
	 * {@link TagResultItem}. After the string is constructed, the text area is
	 * updated
	 * 
	 * @param item
	 */
	private void renderTagInfo(TagResultItem item) {
		StringBuilder details = new StringBuilder();
		details.append("Title: " + item.getTitle() + "\n");
		try {
			details.append("Dislikes: "
					+ this.connection.getNumberOfPreferences(item.getID(),
							PreferenceType.dislike) + "\n");

			details.append("Likes: "
					+ this.connection.getNumberOfPreferences(item.getID(),
							PreferenceType.like) + "\n");
		} catch (RestFulClientException e) {
			new InfoForUser(e.getMessage());
		}

		details.append("Section: " + item.getSectionTitle() + "\n");
		details.append("URL: " + item.getLink() + "\n");

		this.itemInfo.setText(details.toString());

	}

	/**
	 * This public method is responsible for rendering {@link IResultItem}
	 * objects to the display panel. It takes objects that implement this
	 * interface and according to their concrete type, calls the appropriate
	 * rendering methods
	 * 
	 * @param item
	 */
	public void renderContent(IResultItem item) {

		this.currentItem = item;
		this.removeAll(); //strip all the data fromthe pane and recosntruct it
		this.constructTools();
		this.addListeners();

		if (item.getType() == ItemType.content) {
			// if content (aka article), call the article rendering method
			ArticleResultItem article = (ArticleResultItem) item;
			this.renderArticleInfo(article);

		} else {
			//if tag, call the tag rendering  method
			TagResultItem tag = (TagResultItem) item;
			this.renderTagInfo(tag);
		}

		//loop and try to retrive any annotations
		this.itemInfo.append("~~~~ANNOTATIONS~~~~\n");

		try {
			for (IAnnotation ann : this.connection.getAnnotationsForItem(item
					.getID())) {
				this.itemInfo.append(ann + "\n"); //appending all annotations
			}
		} catch (RestFulClientException e) {
			//if sometign goes wrong, inform the user
			new InfoForUser(e.getMessage());

		}

		//finally update UI 
		this.add(this.infoScroll);
		this.updateUI();
	}

	/*
	 * private method abstracting away the construction of the toolbar
	 */
	private void constructTools() {
		tools = new JToolBar();
		like = new JButton("like");
		dislike = new JButton("dislike");

		annotate = new JButton("annotate");
		bookmark = new JButton("bookmark");
		tools.setFloatable(false);
		tools.add(like);
		tools.add(dislike);

		tools.add(annotate);
		tools.add(bookmark);
		tools.setPreferredSize(new Dimension(500, 30));
		this.add(tools, BorderLayout.PAGE_START);

	}

	/**
	 * method providing the functionality for obtaining a reference. It is
	 * useful when creating action listeners
	 * 
	 * @return
	 */
	private DisplayPane getSelf() {
		return this;
	}

	/**
	 * This method is reponsible for adding all the action listeners to the
	 * buttons
	 */
	private void addListeners() {

		this.like.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = currentItem.getID();
				try {
					//expressing a LIKE preference and persisting it to the database
					connection.expressPreference(new Preference(id,
							currentUser, PreferenceType.like));
				} catch (RestFulClientException e1) {
					new InfoForUser(e1.getMessage());

				}
				renderContent(currentItem);

			}
		});

		this.dislike.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String id = currentItem.getID();
				try {
					//expressing a LIKE preference and persisting it to the database

					connection.expressPreference(new Preference(id,
							currentUser, PreferenceType.dislike));
				} catch (RestFulClientException e1) {
					//allerting users if there are errors with the connection
					new InfoForUser(e1.getMessage());

				}
				renderContent(currentItem);
			}
		});

		this.annotate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//on pressing annotate, a new annotation popup is created, which handles the issuing and publishing of annotations
				new AnnotationPopup(currentItem, connection, getSelf());
			}
		});

		this.bookmark.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					/*
					 * adding to favorites, this will cause the connection to
					 * change state and effectively notify its observsers (The
					 * bookmarks pane) that it needs to update itself
					 */
					connection.addItemToFavourites(currentItem, currentUser);
				} catch (RestFulClientException e1) {
					new InfoForUser(e1.getMessage());

				}

			}
		});
	}
}
