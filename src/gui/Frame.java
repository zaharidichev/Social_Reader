package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import model.GuardianReaderModel;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;

public class Frame implements Observer {

	private JFrame fr;
	private Toolbar tools;
	ResultsPane results;
	JScrollPane scroller;
	private GuardianReaderModel model;
	DisplayPane display;
	DBConnection connection;

	public Frame(GuardianReaderModel model, DBConnection connection) {

		this.connection = connection;
		this.model = model;

		this.display = new DisplayPane(connection);

		this.model.addObserver(this);
		this.fr = new JFrame();
		this.tools = new Toolbar(this.model);
		this.results = new ResultsPane(this.display);
		this.scroller = new JScrollPane(results,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		this.fr.add(this.tools, BorderLayout.NORTH);
		this.fr.add(scroller, BorderLayout.CENTER);
		this.constructAdditionalElements();

		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setSize(new Dimension(1200, 800));
		this.updateit();

	}

	private void constructAdditionalElements() {
		Bookmarks personal = new Bookmarks(new BookmarkResultsPane(
				this.display, this.connection));
		try {
			personal.updateBookmarks(connection.retreiveFavourites(connection
					.getLoggedUser()));
		} catch (RestFulClientException e) {
			new ErrorInfo(e.getMessage());

		}
		JPanel sidePane = new JPanel();
		sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.Y_AXIS));
		sidePane.add(display);
		sidePane.add(personal);

		this.fr.add(sidePane, BorderLayout.EAST);

	}

	public void updateit() {

		this.results.update(this.model.getResults());
		this.results.repaint();

		this.fr.repaint();
	}

	public void update(Observable o, Object arg) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateit();

				System.out.println(model.getResults().size());

			}
		});
	}
}
