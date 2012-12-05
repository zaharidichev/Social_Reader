package utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Formatter;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

public class WebUtils {

	public static byte[] getImageFromWeb(String adress) throws IOException {

		URL url = new URL(adress);

		InputStream imageStream = url.openStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		int n = 0;
		while (-1 != (n = imageStream.read(buffer))) {
			output.write(buffer, 0, n);
		}
		imageStream.close();

		byte[] resultData = output.toByteArray();

		return resultData;
	}

	public static JsonNode parseJson(InputStream is)
			throws JsonProcessingException, IOException {
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode = null;
		rootNode = m.readTree(is);
		return rootNode;
	}
	
	public static String bytesToHexString(byte[] bytes) {
	    StringBuilder sb = new StringBuilder(bytes.length * 2);

	    Formatter formatter = new Formatter(sb);
	    for (byte b : bytes) {
	        formatter.format("%02x", b);
	    }

	    return sb.toString();
	}
	
	public static String encodeBytes(byte[] data) { 
		Base64 encoder = new Base64();
		
		
		return encoder.encodeToString(data);
	}
	
	public static byte[] decodeBytes(String data) { 
		Base64 encoder = new Base64();
		
		
		return encoder.decode(data);
	}

}
