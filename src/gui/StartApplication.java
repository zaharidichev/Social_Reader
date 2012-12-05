package gui;

import api.restful.RESTFULContentClient;
import api.userDataPersistance.DBConnection;
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * This is the main entry point of the application. It resolves all the needed
 * Dependencies and displays the login screen
 * 
 * @author 120010516
 * 
 */
public class StartApplication {
	
	private static void  start() { 
		// creates the needed database connection

		IDBConnection connection = new DBConnection("http://localhost", "7050",
				new RESTFULContentClient(), "default");
		new LoginScreen(connection); //creates the login screen
	}

	public static void main(String[] args) {

		start(); // starting
	}

}
