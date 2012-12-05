package utils;
import api.contentRetrival.types.ResultType;

/** 
 * A generic settings class that hold important constants
 * @author 120010516
 *
 */
public class Settings {

	private static final String contentApiAdress = "http://content.guardianapis.com/";
	private static final ResultType defaultResultDataType = ResultType.json;

	public static String getAPIAddress() {
		return contentApiAdress;
	}

	public static ResultType getResultType() {
		return defaultResultDataType;
	}
}
