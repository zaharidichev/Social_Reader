package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A static class that provides several important utilities for downlaoding
 * images from the web, encoding and decoding Base64 ,atc.
 * 
 * @author 120010516
 * 
 */
public class WebUtils {

	/**
	 * This method retrives an image from the web and returns its raw bytes.
	 * Useful in retriving thumbnails
	 * 
	 * @param adress
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * Parses an {@link InputStream} into a Json tree
	 * 
	 * @param is
	 *            the stream
	 * @return {@link JsonNode}
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static JsonNode parseJson(InputStream is)
			throws JsonProcessingException, IOException {
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode = null;
		rootNode = m.readTree(is);
		return rootNode;
	}

	/**
	 * Encodes a byte array into a Base64 string. Useful for storing raw byte
	 * data into NoSQL databases
	 * 
	 * @param data
	 *            the raw bytes
	 * @return
	 */
	public static String encodeBytes(byte[] data) {
		Base64 encoder = new Base64();

		return encoder.encodeToString(data);
	}

	/**
	 * Decodes a base64 {@link String} into a byte[] array. useful for
	 * retrieving binary data from noSQL databases
	 * 
	 * @param data
	 *            the byte data as a base64 {@link String}
	 * @return a byte[]
	 */
	public static byte[] decodeBytes(String data) {
		Base64 encoder = new Base64();

		return encoder.decode(data);
	}

}
