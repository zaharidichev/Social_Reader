package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorInfo {

	private String message;

	public ErrorInfo(String message) {
		this.message = message;

		JOptionPane.showMessageDialog(new JFrame(), this.message);
	}

}
