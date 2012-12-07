package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import model.GuardianReaderModel;
import api.contentRetrival.impl.results.factories.ResultSetFactory;
import api.contentRetrival.interfaces.IResultSetFactory;
import api.restful.RESTFULContentClient;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;
import api.userDataPersistance.interfaces.IDBConnection;
import exceptions.ExistingUserException;
import exceptions.InvalidCredentialsException;

/**
 * This class is used to construct and show a login screen to the user. It
 * contains functionality for logging in and creating new users. It does so by
 * directly communicating with a database connection On successful login, this
 * window is hidden and the main interface of the program is created
 * 
 * @author 120010516
 * 
 */
public class LoginScreen extends JFrame {

	private static final long serialVersionUID = -2589770316116471806L;
	private JButton login; // the login button
	private JButton create; // the create user button
	private JTextField user; // the username field 
	private JTextField pass; // the password field

	private IDBConnection connection = new DBConnection("http://localhost",
			"7050", new RESTFULContentClient(), "default");

	private JToolBar tools;

	/** 
	 * The login screen constructor that takes the connection as an argument
	 * @param connection the connection to the database
	 */
	public LoginScreen(IDBConnection connection ) {
		
		//assigning all needed variables
		this.connection = connection;
		this.tools = new JToolBar();
		this.create = new JButton("Create user");
		this.login = new JButton("Login");
		this.user = new JTextField(50);
		this.pass = new JTextField(50);

		this.tools.add(new JLabel("Username"));
		this.tools.add(user);
		this.tools.add(new JLabel("Password"));
		this.tools.add(pass);

		this.tools.add(login);
		this.tools.add(create);
		this.add(this.tools, BorderLayout.NORTH);

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(1200, 800));
		this.addActionListeners();

	}

	/**
	 * Private method that is used for abstracting the addition of all neded action listeners
	 */
	private void addActionListeners() {
		this.login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//when login is pressed 
				String name = user.getText();
				String passwd = pass.getText();

				boolean successfulLogin = true;
				try {
					try {
						// a login to the connection is attempted
						connection.login(name, passwd);
					} catch (RestFulClientException e1) {
						// if there are connection problems, notify the user
						new InfoForUser(e1.getMessage());

					}
				} catch (InvalidCredentialsException e2) {
					
					//if  the credentials are invalid, notify the user and clear fields
					new InfoForUser(e2.getMessage());
					user.setText("");
					pass.setText("");
					successfulLogin = false;
				}

				if (successfulLogin) {
					//if successful, create a new main frame with the guardian model and the current connection
					setVisible(false);
					try {
						IResultSetFactory factory = new ResultSetFactory();
						new MainAppFrame(new GuardianReaderModel(factory), connection);
					} catch (RestFulClientException e1) {
						
						new InfoForUser(e1.getMessage());

					}
				}

			}
		});

		this.create.addActionListener(new ActionListener() {
			//adding listener for creation of a user
			@Override
			public void actionPerformed(ActionEvent e) {

				String name = user.getText();
				String passwd = pass.getText();

				boolean successfulCreation = true; // keep track of successful completion

				try {
					connection.addUser(name, passwd);
				} catch (ExistingUserException e1) {
					// if there is already such user, notify the client
					successfulCreation = false;
					new InfoForUser("The user is already existing");
					user.setText("");
					pass.setText("");

				} catch (RestFulClientException connectionProblem) {
					// if there are problems with the restful connection, again let the client know
					new InfoForUser(connectionProblem.getMessage());

				}

				if (successfulCreation) {
					//if everything went smoothly, let the client know and clear fields so he/she can login

					new InfoForUser("New profile created!");
					user.setText("");
					pass.setText("");
				}
			}
		});
	}



}
