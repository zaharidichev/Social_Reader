package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import api.contentRetrival.impl.results.Annotation;
import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.interfaces.IDBConnection;

/**
 * This particular class is responsible for handling the user interaction
 * practicalities that invlove creating an annotation object and posting it to
 * the databases
 * 
 * @author 120010516
 * 
 */
public class AnnotationPopup extends JFrame {

	private static final long serialVersionUID = -6047426767503819887L;
	private JButton submit; 
	private JTextArea text;
	private IResultItem item;
	private IDBConnection connection;
	private DisplayPane mainPane;

	/** 
	 * Constructor that takes the required dependencies as arguments
	 * @param currentItem the item to which this annotation is being associated
	 * @param connection and instance of a {@link IDBConnection} 
	 * @param mainPane the display panel that will reflect the addition of this annotation
	 */
	public AnnotationPopup(IResultItem currentItem, IDBConnection connection,
			DisplayPane mainPane) {
		
		//building up the frame  and assigning all variables
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
	
	/* 
	 * Convenience method that adds all the behavior needed to post annotation when pressing SUBMIT
	 */
	private void addSubmitListener() {
		this.submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (text.getText().length() == 0) {
					// if no text type, show a dialog message
					new InfoForUser("You should type some text !");
		

				} else {
					//else create annotation and post it 
					IAnnotation newAnnotation = new Annotation(connection
							.getLoggedUser(), text.getText(), item.getID());
					try {
						connection.addAnnotation(newAnnotation);
					} catch (RestFulClientException e1) {
						//if any exceptions occur, inform the user that there are problems
						new InfoForUser(e1.getMessage());

					}
					setVisible(false);
					mainPane.renderContent(item);
				}

			}
		});
	}



}
