package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import model.GuardianReaderModel;
import api.restful.RESTFULContentClient;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;
import exceptions.ExistingUserException;
import exceptions.InvalidCredentialsException;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = -2589770316116471806L;
	private JButton login;
	private JButton create;
	private JTextField user;
	private JTextField pass;

	private DBConnection connection = new DBConnection("http://localhost",
			"7050", new RESTFULContentClient(), "default");

	private JToolBar tools;

	public LoginScreen() {

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

	private void addActionListeners() {
		this.login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = user.getText();
				String passwd = pass.getText();

				boolean successfulLogin = true;
				try {
					try {
						connection.login(name, passwd);
					} catch (RestFulClientException e1) {
						new ErrorInfo(e1.getMessage());

					}
				} catch (InvalidCredentialsException e2) {

					new ErrorInfo(e2.getMessage());
					user.setText("");
					pass.setText("");
					successfulLogin = false;
				}

				if (successfulLogin) {
					setVisible(false);
					try {
						new Frame(new GuardianReaderModel(), connection);
					} catch (RestFulClientException e1) {
						new ErrorInfo(e1.getMessage());

					}
				}

			}
		});

		this.create.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = user.getText();
				String passwd = pass.getText();

				boolean successfulCreation = true;

				try {
					connection.addUser(name, passwd);
				} catch (ExistingUserException e1) {
					successfulCreation = false;
					JOptionPane.showMessageDialog(new JFrame(), e1.getMessage());
					user.setText("");
					pass.setText("");

				} catch (RestFulClientException connectionProblem) {
					new ErrorInfo(connectionProblem.getMessage());

				}

				if (successfulCreation) {
					JOptionPane.showMessageDialog(new JFrame(),
							"New profile created!");
					user.setText("");
					pass.setText("");
				}
			}
		});
	}

	public static void main(String args[]) {
		new LoginScreen();
	}

}
