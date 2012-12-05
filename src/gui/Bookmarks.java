package gui;

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import api.contentRetrival.interfaces.IResultItem;

/**
 * This is the bookmarks UI component that holds {@link ResultsPane} object
 * 
 * @author 120010516
 * 
 */
public class Bookmarks extends JPanel {

	private static final long serialVersionUID = 1450458295491658080L;
	private JToolBar tools;
	private JLabel label;
	private ResultsPane results;

	/**
	 * Constructor that takes in the {@link ResultsPane} object tat will be
	 * displayed in this component
	 * 
	 * @param results
	 */
	public Bookmarks(ResultsPane results) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.constructToolBar();
		this.results = results;

		//putting the result pane into a scroller
		JScrollPane scroller = new JScrollPane(this.results,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scroller);

	}

	/**
	 * This method simply calls the {@link ResultsPane} update method
	 * 
	 * @param items
	 */
	public void updateBookmarks(LinkedList<IResultItem> items) {
		this.results.update(items);
	}

	/*
	 * Convenience method to abstract away the creation of the toolbar
	 */
	private void constructToolBar() {
		tools = new JToolBar();
		label = new JLabel("Favourites");
		tools.add(label);
		tools.setPreferredSize(new Dimension(500, 30));

		this.setPreferredSize(new Dimension(520, 300));

		this.add(tools);

	}

}
