import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TIO {
	
	public static String getLanguageURL(String language) throws IOException {
		String indexContent = Request.Get("http://tryitonline.net").execute().returnContent().asString();
		Pattern regex = Pattern.compile("<li><a href=\"(.+)\">(.+)</a></li>");
		Matcher matcher = regex.matcher(indexContent);
		while(matcher.find()) {
			if(matcher.group(2).equalsIgnoreCase(language)) {
				return "http:" + matcher.group(1);
			}
		}
		throw new IOException();
	}
	
	public static String getPermalink(String language, String code) throws IOException {
		return getPermalink(language, code, "", "", false);
	}
	
	public static String getPermalink(String language, String code, String input) throws IOException {
		return getPermalink(language, code, input, "", false);
	}
	
	public static String getPermalink(String language, String code, String input, String args) throws IOException {
		return getPermalink(language, code, input, args, false);
	}

	public static String getPermalink(String language, String code, String input, String args, boolean debug) throws IOException {
		String baseURL = getLanguageURL(language) + "#";
		String codeParam = "code=" + URLEncoder.encode(code, "UTF-8");
		String inputParam = "&input=" + URLEncoder.encode(input, "UTF-8");
		String argsParam = "&args=" + URLEncoder.encode(args, "UTF-8");
		String debugParam = debug?"&debug=on":"";
		String URL = baseURL + codeParam + inputParam + argsParam + debugParam;
		return URL;
	}
	
	public static String getResult(String language, String code) throws IOException {
		return getResult(language, code, "", "", false);
	}
	
	public static String getResult(String language, String code, String input) throws IOException {
		return getResult(language, code, input, "", false);
	}
	
	public static String getResult(String language, String code, String input, String args) throws IOException {
		return getResult(language, code, input, args, false);
	}
	
	public static String getResult(String language, String code, String input, String args, boolean debug) throws IOException {
		return Request.Post(getLanguageURL(language)+"cgi-bin/backend")
		.bodyForm(
				Form.form()
				.add("code", URLEncoder.encode(code, "UTF-8"))
				.add("input", URLEncoder.encode(input, "UTF-8"))
				.add("args", URLEncoder.encode(args, "UTF-8"))
				.add("debug", debug?"on":"")
				.build())
		.execute()
		.returnContent()
		.asString()
		.substring(33);
	}
	
}
