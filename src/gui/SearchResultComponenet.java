package gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.types.ItemType;

public class SearchResultComponenet extends JPanel {

	private static final long serialVersionUID = 1L;
	private IResultItem resultItem;
	private JLabel title;
	private JLabel type;

	public SearchResultComponenet(IResultItem result) {
		super();

		this.resultItem = result;
		String type = null;
		if (resultItem.getType() == ItemType.content) {
			type = "Article";
		} else {
			type = "Tag";
		}

		//this.setPreferredSize(new Dimension(400, 50));
		this.type = new JLabel("Type: " + type);
		this.title = new JLabel("Title: " + resultItem.getTitle());

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(this.title);
		this.add(this.type);

		this.setVisible(true);
		
	}

}
