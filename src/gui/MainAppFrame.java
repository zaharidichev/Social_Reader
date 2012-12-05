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
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * This class is the main application screen that encapsulates all the different
 * components that the user needs to interact with after logging in.
 * 
 * @author 120010516
 * 
 */
public class MainAppFrame implements Observer {

	private JFrame fr;
	private Toolbar tools; // the toolbar
	private ResultsPane results; // the result pane 
	private JScrollPane scroller; // the scroller that will hold the result pane
	private GuardianReaderModel model; // the model that will be used for data retrieval
	private DisplayPane display;
	private IDBConnection connection;

	/**
	 * Public constructor that needs two dependencies, an {@link IDBConnection}
	 * and a {@link GuardianReaderModel}. The first one is for persisting user
	 * data and the second for retrieving content from the guardian API
	 * 
	 * @param model
	 * @param connection
	 */
	public MainAppFrame(GuardianReaderModel model, IDBConnection connection) {

		this.connection = connection;
		this.model = model;

		this.display = new DisplayPane(connection);

		this.model.addObserver(this); //adding itself as an observer to the model
		//setting sizing and layout
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

	/*
	 * private helper method that constructs the additional elements needed
	 */
	private void constructAdditionalElements() {
		Bookmarks personal = new Bookmarks(new BookmarkResultsPane(
				this.display, this.connection));
		try {
			//after the creation of a bookmark pane, an update is attempted so all the user data that is already stored is retreived
			personal.updateBookmarks(connection.retreiveFavourites(connection
					.getLoggedUser()));
		} catch (RestFulClientException e) {
			new InfoForUser(e.getMessage());

		}
		JPanel sidePane = new JPanel();
		sidePane.setLayout(new BoxLayout(sidePane, BoxLayout.Y_AXIS));
		sidePane.add(display);
		sidePane.add(personal);

		this.fr.add(sidePane, BorderLayout.EAST);

	}

	/**
	 * Updates the main frame and its components
	 */
	public void updateit() {

		this.results.update(this.model.getResults());
		this.results.repaint();

		this.fr.repaint();
	}

	
	@Override
	public void update(Observable o, Object arg) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateit();
			}
		});
	}
}
