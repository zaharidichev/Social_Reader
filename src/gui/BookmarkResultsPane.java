package gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * This class is an extension of {@link ResultsPane}. It is useful for
 * displaying the bookmarks of a particular user.It also implements
 * {@link Observer} which allows it to update itself if the state of the
 * connection changes. This state would change if an annotation for example is
 * posted
 * 
 * @author 120010516
 * 
 */
public class BookmarkResultsPane extends ResultsPane implements Observer {

	private static final long serialVersionUID = 3230034343409536541L;
	private DBConnection connection;

	/**
	 * Constructor that takes in the required dependencies
	 * 
	 * @param display
	 * @param connection
	 *            the connection which is being used
	 */
	public BookmarkResultsPane(DisplayPane display, IDBConnection connection) {

		super(display);
		this.connection = (DBConnection) connection;
		//adds itself as an observer of the connection
		this.connection.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//on update try to retrive all the favourites of the user and redisplay them
				try {
					update(connection.retreiveFavourites(connection
							.getLoggedUser()));
				} catch (RestFulClientException e) {
					new InfoForUser(e.getMessage());

				}
			}
		});
	}

}
