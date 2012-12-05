package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class is responsible for delivering a message to the user in the form of
 * a info popup. It is useful to notify the users for eventual errors or
 * problems with the system. This is the end point of propagated exceptions, as
 * they usually indicate problem with the connection. In such times, te user is
 * notified using this popup mechanism
 * 
 * @author 120010516
 * 
 */
public class InfoForUser {

	private String message;

	public InfoForUser(String message) {
		this.message = message;
		// creating a new info popup
		JOptionPane.showMessageDialog(new JFrame(), this.message);
	}

}
