package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import model.GuardianReaderModel;
import api.contentRetrival.impl.requests.ContentSearchRequest;
import api.contentRetrival.impl.requests.TagSearchRequest;
import api.contentRetrival.interfaces.ISearchRequest;
import api.contentRetrival.types.ItemType;
import api.restful.exceptions.RestFulClientException;

/**
 * This is the main toolbar of the application. It is used to issues search
 * requests for various item types.
 * 
 * @author 120010516
 * 
 */
public class Toolbar extends JToolBar implements Observer {

	private static final long serialVersionUID = 2727301245849672295L;
	private ItemType searchOperation;
	private JButton search;
	private JButton previous;
	private JButton next;

	private JTextField keyword;
	private JTextField section;
	private JTextField tag;

	private JRadioButton articleSearch;
	private JRadioButton tagSearch;
	private GuardianReaderModel model;

	private ButtonGroup operations;

	/**
	 * A constructor that takes as an argument the guardian model
	 * 
	 * @param model
	 *            {@link GuardianReaderModel} an instance of a guardian data
	 *            retrieval model
	 */
	public Toolbar(GuardianReaderModel model) {
		super();

		// adding buttons and fields and initialising variables
		this.model = model;
		this.initVariables();
		this.constructRadioButtons();

		this.add(new JLabel("Keyword"));
		this.add(this.keyword);

		this.add(new JLabel("Section"));
		this.add(this.section);

		this.add(new JLabel("Tag"));
		this.add(this.tag);

		this.add(this.search);
		this.add(this.previous);
		this.add(this.next);

		this.addListeners();

	}

	/*
	 * private method tat abstracts the intiation of variables, used in the
	 * constructor
	 */
	private void initVariables() {
		this.search = new JButton("Search");
		this.previous = new JButton("Previous page");
		this.next = new JButton("Next page");
		this.keyword = new JTextField();
		this.section = new JTextField();
		this.tag = new JTextField();

	}

	/*
	 * Private method that adds all the necessary listeners to the buttons
	 */
	private void addListeners() {
		this.search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ISearchRequest request = createSearchRequest();
				try {
					//when a search is performed, construct a request and issue it to the model
					model.searchForContent(request);
				} catch (RestFulClientException e1) {
					new InfoForUser(e1.getMessage());
				}
			}
		});

		this.next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//when the next page is requested, issue a request to the model
					model.getNextPage();
				} catch (RestFulClientException e1) {
					new InfoForUser(e1.getMessage());
				}
			}
		});

		this.previous.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.getPrevPage();
					//same as next page, but for the previous one
				} catch (RestFulClientException e1) {
					new InfoForUser(e1.getMessage());
				}
			}
		});
	}

	/*
	 * Private method to abstract away the creation of a search request. It
	 * takes all the avaiable data from the text fields and constructs an
	 * appropriate search request object
	 */
	private ISearchRequest createSearchRequest() {

		String keyword = this.keyword.getText();
		if (this.searchOperation == ItemType.tag) {
			return new TagSearchRequest(keyword);
		}
		ContentSearchRequest request = new ContentSearchRequest(keyword);
		if (this.tag.getText() != "") {
			request.setTagFilter(this.tag.getText());
		}
		if (this.section.getText() != "") {
			request.setSectionFilter(this.tag.getText());
		}
		return request;
	}

	/*
	 * This method abstract the practicalities of constructing the radio buttons
	 * and handling the dependencies between their actions
	 */
	private void constructRadioButtons() {
		this.operations = new ButtonGroup();
		this.articleSearch = new JRadioButton("Article search");
		this.tagSearch = new JRadioButton("Tag search");
		articleSearch.setSelected(true);
		articleSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (articleSearch.isSelected()) {
					section.setEditable(true);
					tag.setEditable(true);
					searchOperation = ItemType.content;
				}
			}
		});

		tagSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tagSearch.isSelected()) {
					section.setEditable(false);
					tag.setEditable(false);
					searchOperation = ItemType.tag;
				}
			}
		});
		operations.add(this.articleSearch);
		operations.add(this.tagSearch);
		this.add(articleSearch);
		this.add(tagSearch);

	}

	@Override
	public void update(Observable o, Object arg) {

	}

}
