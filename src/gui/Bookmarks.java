package gui;

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import api.contentRetrival.interfaces.IResultItem;

public class Bookmarks extends JPanel {

	private static final long serialVersionUID = 1450458295491658080L;
	private JToolBar tools;
	JLabel label;
	ResultsPane results;

	public Bookmarks(ResultsPane results) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.constructToolBar();
		this.results = results;

		JScrollPane scroller = new JScrollPane(this.results,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scroller);

	}

	public void updateBookmarks(LinkedList<IResultItem> items) {
		this.results.update(items);
	}

	private void constructToolBar() {
		tools = new JToolBar();
		label = new JLabel("Favourites");
		tools.add(label);
		tools.setPreferredSize(new Dimension(500, 30));

		this.setPreferredSize(new Dimension(520, 300));

		this.add(tools);

	}

}
