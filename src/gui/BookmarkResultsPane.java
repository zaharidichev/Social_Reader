package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;

public class BookmarkResultsPane extends ResultsPane implements Observer {

	private static final long serialVersionUID = 3230034343409536541L;
	DBConnection connection;

	public BookmarkResultsPane(DisplayPane display, DBConnection connection) {

		super(display);
		this.connection = connection;
		this.connection.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					update(connection.retreiveFavourites(connection
							.getLoggedUser()));
				} catch (RestFulClientException e) {
					new ErrorInfo(e.getMessage());

				}
			}
		});
	}

}
