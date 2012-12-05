package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import api.contentRetrival.interfaces.IResultItem;

/**
 * This class is responsible for rendering all the results that are being
 * returned from a search request. It takes as a reference a {@link DisplayPane}
 * in order to be able to send a particular item of interest to it for detailed
 * rendering
 * 
 * @author 120010516
 * 
 */
public class ResultsPane extends JPanel {

	private static final long serialVersionUID = -5961855958524760651L;

	private JList sampleJList; // a jlist of all results
	private LinkedList<IResultItem> items; // all the items
	private DisplayPane display; // the display pane

	/**
	 * Constructor that initialises all the needed variables and injects the
	 * single dependency
	 * 
	 * @param displayPane
	 */
	public ResultsPane(DisplayPane displayPane) {

		super();
		this.display = displayPane;

		//setting up the appearance of the list
		sampleJList = new JList();
		sampleJList.setVisibleRowCount(5);
		sampleJList.setBackground(Color.DARK_GRAY);
		sampleJList.setForeground(Color.GREEN);
		Font displayFont = new Font("Serif", Font.BOLD, 18);
		sampleJList.setFont(displayFont);
		this.add(sampleJList);

		//adding a listener that will take care of updating the display pane when the user clich on a particular item of interest
		sampleJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
						// sending the item to the display pane
						IResultItem item = items.get(sampleJList
								.getSelectedIndex());
						display.renderContent(item);

					} catch (IndexOutOfBoundsException e2) {
						// really do nothing. This is side effect of adjusting
						// the list value
					}
				}

			}
		});

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	/**
	 * This method is used for refreshing the result list. It is used when a new
	 * search is conducted or when the pages are flipped.
	 * 
	 * @param items
	 */
	public void update(LinkedList<IResultItem> items) {
		this.items = items;
		Object[] textArray = items.toArray();
		ListModel lmodel = new DefaultComboBoxModel(textArray); // updating the model of the list

		this.sampleJList.setModel(lmodel);

		this.sampleJList.repaint();
		this.repaint(); // repainting the componenet

	}

}
