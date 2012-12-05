package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {


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
