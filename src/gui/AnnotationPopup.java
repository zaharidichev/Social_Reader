package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import api.contentRetrival.impl.results.Annotation;
import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.DBConnection;

public class AnnotationPopup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6047426767503819887L;
	private JButton submit;
	private JTextArea text;
	private IResultItem item;
	private DBConnection connection;
	private DisplayPane mainPane;

	public AnnotationPopup(IResultItem currentItem, DBConnection connection,
			DisplayPane mainPane) {
		this.item = currentItem;
		this.mainPane = mainPane;
		this.item = currentItem;
		this.connection = connection;
		this.submit = new JButton("submit");
		this.text = new JTextArea();
		this.add(submit, BorderLayout.NORTH);
		this.add(text, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(400, 400));
		this.addSubmitListener();
	}

	public void addSubmitListener() {
		this.submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (text.getText().length() == 0) {
					JOptionPane.showMessageDialog(new JFrame(),
							"You should type some text !");

				} else {
					IAnnotation newAnnotation = new Annotation(connection
							.getLoggedUser(), text.getText(), item.getID());
					try {
						connection.addAnnotation(newAnnotation);
					} catch (RestFulClientException e1) {
						new ErrorInfo(e1.getMessage());

					}
					setVisible(false);
					mainPane.renderContent(item);
				}

			}
		});
	}

	public static void main(String[] args) {
	}

}
