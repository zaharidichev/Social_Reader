package restful;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class RESTFULContentClient {

	HttpClient client;

	static String readString(InputStream is) throws IOException {
		char[] buf = new char[2048];
		Reader r = new InputStreamReader(is, "UTF-8");
		StringBuilder s = new StringBuilder();
		while (true) {
			int n = r.read(buf);
			if (n < 0)
				break;
			s.append(buf, 0, n);
		}
		return s.toString();
	}

	public RESTFULContentClient() {
		this.client = new DefaultHttpClient();
	}

	public InputStream executeRequest(String address)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpResponse res = null;
		URI httpGetURI = new URI(address);
		HttpGet get = new HttpGet(httpGetURI);
		res = this.client.execute(get);
		InputStream is = null;

		if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
		}
		HttpEntity entity = res.getEntity();

		if (entity != null) {
			is = entity.getContent();
		}
		return is;
	}

	public void postJson(String adress, String content)
			throws URISyntaxException {
		HttpPost post = new HttpPost(new URI(adress));

		StringEntity entity;
		try {
			entity = new StringEntity(content);
			entity.setContentType("application/json");
			post.setEntity(entity);
			client.execute(post);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}

	}

}
