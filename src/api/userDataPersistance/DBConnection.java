package api.userDataPersistance;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import org.codehaus.jackson.JsonNode;

import utils.WebUtils;
import api.contentRetrival.impl.results.Annotation;
import api.contentRetrival.impl.results.ArticleResultItem;
import api.contentRetrival.impl.results.TagResultItem;
import api.contentRetrival.interfaces.IAnnotation;
import api.contentRetrival.interfaces.IResultItem;
import api.contentRetrival.types.ItemType;
import api.restful.IRESTFULClient;
import api.restful.exceptions.RestFulClientException;
import api.userDataPersistance.interfaces.IDBConnection;
import api.userDataPersistance.interfaces.IPreference;
import exceptions.ExistingUserException;
import exceptions.InvalidCredentialsException;

/**
 * An implementation of the {@link IDBConnection} interface, this class was
 * written to communicate with JasDb and implement all the methods of the
 * interface accordingly.This class provides the functionality that i s need
 * to persist user data onto the database
 * @author 120010516
 *
 */
public class DBConnection extends Observable implements IDBConnection {

	private String address;
	private String port;
	private IRESTFULClient client;
	private String instance;
	private String username;

/** 
 * Constructor providing all the necessary dependancies for the connection
 * @param adress the adress (host)
 * @param port the port 
 * @param restFulClient the {@link IRESTFULClient} object
 * @param instance the instance of the database 
 */
	public DBConnection(String adress, String port,
			IRESTFULClient restFulClient, String instance) {

		this.address = adress;
		this.port = port;
		this.client = restFulClient;
		this.instance = instance;
	}

	@Override
	public void login(String user, String password)
			throws InvalidCredentialsException, RestFulClientException {
		if (!this.areCredentialsValid(user, password)) {
			throw new InvalidCredentialsException("Wrong user credentials");
		} else {
			this.username = user;
		}

	}

	@Override
	public boolean doesBagExist(String bagName) throws RestFulClientException {
		// http://localhost:7050/Bags(users)/

		String querry = this.address + ":" + port + "/Bags(" + bagName + ")";
		boolean result = false;
		System.out.println("q" +  querry);
		InputStream data = client.executeRequest(querry);
		JsonNode node;
		try {
			node = WebUtils.parseJson(data);
		} catch (Exception e) {
			throw new RestFulClientException();
		}
		if (node.path("statusCode").getIntValue() != 404) {
			result = true;

		}

		return result;
	}

	@Override
	public void createBag(String bagName) throws RestFulClientException {
		String qAddress = this.address + ":" + port + "/Bags";

		String data = "{" + wrap("instanceId") + ":" + wrap(this.instance)
				+ ",\"name\":" + wrap(bagName) + "}";

		System.out.println(data);
		this.client.postJson(qAddress, data);
	}

	@Override
	public void createBagIfNotThere(String bagName)
			throws RestFulClientException {
		if (!doesBagExist(bagName)) {

			this.createBag(bagName);
		}
	}

	@Override
	public void addContentToBag(String jsonData, String bagName)
			throws RestFulClientException {

		String querry = this.address + ":" + port + "/Bags(" + bagName
				+ ")/Entities";

		System.out.println(querry);
		this.client.postJson(querry, jsonData);

		// http://localhost:7050/Bags(wikidata)/Entities

	}

	private String wrap(String text) {
		return "\"" + text + "\"";
	}

	@Override
	public void addUser(String name, String pass) throws ExistingUserException,
			RestFulClientException {

		if (this.doesUserExist(name)) {
			throw new ExistingUserException("Username already exists");
		}
		this.createBagIfNotThere("Users");
		String data = "{\"userName\":" + wrap(name) + ",\"passwd\":"
				+ wrap(pass) + "}";

		this.addContentToBag(data, "Users");

	}

	@Override
	public boolean doesUserExist(String username) throws RestFulClientException {
		// http://localhost:7050/Bags(Users)/Entities(userName=effects)

		String querry = this.address + ":" + port
				+ "/Bags(Users)/Entities(userName=" + username + ")";

		System.out.println(querry);
		boolean result = false;
		InputStream data;

		data = client.executeRequest(querry);
		JsonNode node;
		try {
			node = WebUtils.parseJson(data);
		} catch (Exception e) {
			throw new RestFulClientException("Connection problem:"
					+ e.getMessage());
		}
		if (node.path("size").getIntValue() > 0) {
			result = true;
		}

		return result;

	}

	/* 
	 * Convinience method to parse the result of the restful client into a json structured data
	 */
	private StringBuilder ConstructJsonForFavourites(IResultItem item) {
		StringBuilder json = new StringBuilder();
		if (item.getType() == ItemType.tag) {
			TagResultItem tag = (TagResultItem) item;

			json.append("{");
			json.append(wrap("title") + ": " + wrap(tag.getTitle()) + ",");
			json.append(wrap("type") + ": " + wrap("tag") + ",");
			json.append(wrap("section") + ": " + wrap(tag.getSectionTitle())
					+ ",");
			json.append(wrap("url") + ": " + wrap(tag.getLink()) + ",");
			json.append(wrap("id") + ": " + wrap(tag.getID()) + ",");
			return json;
		} else {
			ArticleResultItem article = (ArticleResultItem) item;
			json.append("{");
			json.append(wrap("title") + ": " + wrap(article.getTitle()) + ",");
			json.append(wrap("type") + ": " + wrap("article") + ",");
			json.append(wrap("section") + ": "
					+ wrap(article.getSectionTitle()) + ",");

			json.append(wrap("date") + ": " + wrap(article.getDateAsText())
					+ ",");

			json.append(wrap("url") + ": " + wrap(article.getLink()) + ",");
			json.append(wrap("id") + ": " + wrap(article.getID()) + ",");
			json.append(wrap("author") + ": " + wrap(article.getAuthor()) + ",");
			String base64Image = WebUtils.encodeBytes(article.getThumbnail());
			json.append(wrap("image") + ": " + wrap(base64Image) + ",");
			return json;
		}

	}

	@Override
	public boolean isInFavourites(IResultItem item, String user)
			throws RestFulClientException {
		createBagIfNotThere("Favourites");
		String querry = this.address + ":" + port
				+ "/Bags(Favourites)/Entities(id=" + item.getID() + ",user="
				+ user + ")";

		System.out.println(querry);

		boolean result = false;
		InputStream data;

		data = client.executeRequest(querry);
		JsonNode node;
		try {
			node = WebUtils.parseJson(data);
		} catch (Exception e) {
			throw new RestFulClientException("Connection error: "
					+ e.getMessage());
		}
		if (node.path("size").getIntValue() > 0)
			result = true;

		return result;

	}

	@Override
	public void addItemToFavourites(IResultItem item, String user)
			throws RestFulClientException {

		if (!this.isInFavourites(item, user)) {
			StringBuilder json = this.ConstructJsonForFavourites(item);
			json.append(wrap("user") + ": " + wrap(user));
			json.append("}");
			// http://localhost:7050/Bags(wikidata)/Entities

			String postAddress = this.address + ":" + port
					+ "/Bags(Favourites)/Entities";

			System.out.println(json.toString());
			this.client.postJson(postAddress, json.toString());

			this.setChanged();
			this.notifyObservers();
		}

	}

	@Override
	public LinkedList<IResultItem> retreiveFavourites(String username)
			throws RestFulClientException {
		this.createBagIfNotThere("Favourites");
		LinkedList<IResultItem> result = new LinkedList<IResultItem>();
		String qAdress = this.address + ":" + port
				+ "/Bags(Favourites)/Entities(user=" + username + ")";

		InputStream response;
		try {
			response = client.executeRequest(qAdress);
			JsonNode rootNode = WebUtils.parseJson(response);

			Iterator<JsonNode> elements = rootNode.path("entities")
					.getElements();

			while (elements.hasNext()) {
				JsonNode next = elements.next().path("properties");
				String type = next.path("type").getTextValue();

				String title = next.path("title").getTextValue();
				String section = next.path("section").getTextValue();
				String url = next.path("url").getTextValue();
				String id = next.path("id").getTextValue();

				if (type.equals("tag")) {

					TagResultItem tag = new TagResultItem(id, title, section,
							url);
					result.add(tag);

				} else {

					String date = next.path("date").getTextValue();

					String author = next.path("author").getTextValue();
					byte[] image = WebUtils.decodeBytes(next.path("image")
							.getTextValue());

					ArticleResultItem article = new ArticleResultItem(title,
							section, id, date, url, author, image);

					result.add(article);
				}

			}

			System.out.println(qAdress);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean areCredentialsValid(String name, String password)
			throws RestFulClientException {
		this.createBagIfNotThere("Users");

		String querry = this.address + ":" + port
				+ "/Bags(Users)/Entities(userName=" + name + ",passwd="
				+ password + ")";

		System.out.println(querry);
		boolean result = false;
		InputStream data;
		try {
			data = client.executeRequest(querry);
			JsonNode node = WebUtils.parseJson(data);
			if (node.path("size").getIntValue() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void expressPreference(IPreference preference)
			throws RestFulClientException {
		this.createBagIfNotThere("Likes");
		this.createBagIfNotThere("Dislikes");

		String data = "{\"uid\":" + wrap(preference.getItemUID())
				+ ",\"user\":" + wrap(preference.getUser()) + "}";

		if (preference.getType() == PreferenceType.like) {
			this.addContentToBag(data, "Likes");

		} else {
			this.addContentToBag(data, "Dislikes");

		}
	}

	@Override
	public int getNumberOfPreferences(String UID, PreferenceType type)
			throws RestFulClientException {
		this.createBagIfNotThere("Likes");
		this.createBagIfNotThere("Dislikes");

		String bagName = (type == PreferenceType.like) ? "Likes" : "Dislikes";
		String querry = this.address + ":" + port + "/Bags(" + bagName
				+ ")/Entities(uid=" + UID + ")";

		System.out.println(querry);
		InputStream likes;
		try {
			likes = this.client.executeRequest(querry);
			JsonNode rootNode = WebUtils.parseJson(likes);

			System.out.println("Likes: " + rootNode.path("size").getIntValue()
					+ " UID " + UID);
			return rootNode.path("size").getIntValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;

	}

	@Override
	public void addAnnotation(IAnnotation annotation)
			throws RestFulClientException {
		this.createBagIfNotThere("Annotations");

		String data = "{\"uid\":" + wrap(annotation.getUID()) + ",\"user\":"
				+ wrap(annotation.getUserName()) + ",\"text\":"
				+ wrap(annotation.getText()) + "}";

		this.addContentToBag(data, "Annotations");

	}

	@Override
	public LinkedList<IAnnotation> getAnnotationsForItem(String UID)
			throws RestFulClientException {
		this.createBagIfNotThere("Annotations");

		LinkedList<IAnnotation> annotations = new LinkedList<IAnnotation>();

		String querry = this.address + ":" + port
				+ "/Bags(Annotations)/Entities(uid=" + UID + ")";

		InputStream resultData;
		try {
			resultData = this.client.executeRequest(querry);
			JsonNode rootNode = WebUtils.parseJson(resultData);

			Iterator<JsonNode> elements = rootNode.path("entities")
					.getElements();

			while (elements.hasNext()) {
				JsonNode elemProps = elements.next().path("properties");

				String eUID = elemProps.path("uid").getTextValue();
				String eUser = elemProps.path("user").getTextValue();
				String eText = elemProps.path("text").getTextValue();

				Annotation currentAnnotation = new Annotation(eUser, eText,
						eUID);
				annotations.add(currentAnnotation);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return annotations;

	}

	@Override
	public String getLoggedUser() {
		return this.username;
	}

}
