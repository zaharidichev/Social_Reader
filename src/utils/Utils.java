package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *  A static class containing useful utilities
 * 
 * @author 120010516
 * 
 */
public class Utils {

	/**
	 * A method that reads a file and returns the bytes
	 * @param path path to the file
	 * @return the raw bytes
	 */
	public static byte[] getMockResponse(String path) {
		File file = new File(path);
		char[] chars = new char[(int) file.length()];
		FileReader reader = null;
		try {
			reader = new FileReader(file);

			reader.read(chars);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(chars).getBytes();

	}
}
