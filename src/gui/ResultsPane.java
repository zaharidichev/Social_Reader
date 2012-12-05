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

public class ResultsPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5961855958524760651L;

	private JList<Object> sampleJList;
	private LinkedList<IResultItem> items;
	private DisplayPane display;

	public ResultsPane(DisplayPane displayPane) {

		super();
		this.display = displayPane;

		sampleJList = new JList<Object>();
		sampleJList.setVisibleRowCount(5);
		sampleJList.setBackground(Color.DARK_GRAY);
		sampleJList.setForeground(Color.GREEN);
		Font displayFont = new Font("Serif", Font.BOLD, 18);
		sampleJList.setFont(displayFont);
		this.add(sampleJList);

		sampleJList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
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

	public void update(LinkedList<IResultItem> items) {
		this.items = items;
		Object[] textArray = items.toArray();
		ListModel<Object> lmodel = new DefaultComboBoxModel<Object>(textArray);

		this.sampleJList.setModel(lmodel);

		this.sampleJList.repaint();
		this.repaint();

	}

}
