package utils;
import api.contentRetrival.types.ResultType;

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
