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
import api.userDataPersistance.DBConnection;
import api.userDataPersistance.Preference;
import api.userDataPersistance.PreferenceType;

public class DisplayPane extends JPanel {

	private static final long serialVersionUID = -5710893582352964181L;
	private JToolBar tools;
	private JTextArea itemInfo;
	private JScrollPane infoScroll;
	DBConnection connection;

	IResultItem currentItem;

	JButton like;
	JButton dislike;

	JButton annotate;
	JButton bookmark;
	String currentUser;

	public DisplayPane(DBConnection connection) {
		this.connection = connection;
		this.currentUser = this.connection.getLoggedUser();

		this.constructTools();

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

	private void renderArticleInfo(ArticleResultItem item) {

		if (item.getThumbnail().length > 0) {
			System.out.println(item.getThumbnail()[7]);
			InputStream in = new ByteArrayInputStream(item.getThumbnail());
			BufferedImage thumb;
			Component picLabel = null;

			try {
				thumb = ImageIO.read(in);
				picLabel = new JLabel(new ImageIcon(thumb));

			} catch (IOException e) {
				new ErrorInfo(e.getMessage());
			}
			this.add(picLabel, BorderLayout.PAGE_START);
			this.updateUI();
		}

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
			new ErrorInfo(e.getMessage());

		}

		details.append("Section: " + item.getSectionTitle() + "\n");
		details.append("URL: " + item.getLink() + "\n");

		this.itemInfo.setText(details.toString());

	}

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
			new ErrorInfo(e.getMessage());
		}

		details.append("Section: " + item.getSectionTitle() + "\n");
		details.append("URL: " + item.getLink() + "\n");

		this.itemInfo.setText(details.toString());

	}

	public void renderContent(IResultItem item) {
		this.currentItem = item;
		this.removeAll();
		this.constructTools();
		this.addListeners();

		if (item.getType() == ItemType.content) {
			ArticleResultItem article = (ArticleResultItem) item;
			this.renderArticleInfo(article);

		} else {
			System.out.println("taaag");
			TagResultItem tag = (TagResultItem) item;
			this.renderTagInfo(tag);
		}

		this.itemInfo.append("~~~~ANNOTATIONS~~~~\n");

		try {
			for (IAnnotation ann : this.connection.getAnnotationsForItem(item
					.getID())) {
				this.itemInfo.append(ann + "\n");
			}
		} catch (RestFulClientException e) {
			new ErrorInfo(e.getMessage());

		}

		this.add(this.infoScroll);
		this.updateUI();
	}

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

	public DisplayPane getSelf() {
		return this;
	}

	public void addListeners() {

		this.like.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = currentItem.getID();
				try {
					connection.expressPreference(new Preference(id,
							currentUser, PreferenceType.like));
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());

				}
				renderContent(currentItem);

			}
		});

		this.dislike.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String id = currentItem.getID();
				try {
					connection.expressPreference(new Preference(id,
							currentUser, PreferenceType.dislike));
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());

				}
				renderContent(currentItem);
			}
		});

		this.annotate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new AnnotationPopup(currentItem, connection, getSelf());
			}
		});

		this.bookmark.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					connection.addItemToFavourites(currentItem, currentUser);
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());

				}

			}
		});
	}
}
