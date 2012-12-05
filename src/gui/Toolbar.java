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

public class Toolbar extends JToolBar implements Observer {

	private static final long serialVersionUID = 2727301245849672295L;
	private ItemType searchOperation;
	JButton search;
	JButton previous;
	JButton next;

	private JTextField keyword;
	private JTextField section;
	private JTextField tag;

	JRadioButton articleSearch;
	JRadioButton tagSearch;
	private GuardianReaderModel model;

	private ButtonGroup operations;

	public Toolbar(GuardianReaderModel model) {
		super();

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

	private void initVariables() {
		this.search = new JButton("Search");
		this.previous = new JButton("Previous page");
		this.next = new JButton("Next page");

		this.keyword = new JTextField();
		this.section = new JTextField();
		this.tag = new JTextField();

	}

	private void addListeners() {
		this.search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ISearchRequest request = createSearchRequest();
				try {
					model.searchForContent(request);
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());
				}
			}
		});

		this.next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.getNextPage();
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());
				}
			}
		});

		this.previous.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.getPrevPage();
				} catch (RestFulClientException e1) {
					new ErrorInfo(e1.getMessage());
				}
			}
		});
	}

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
