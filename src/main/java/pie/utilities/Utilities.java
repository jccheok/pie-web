package pie.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class Utilities {

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final DateFormat clientDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static final DateFormat servletDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Date parseClientDate(String date) throws ParseException {
		return clientDateFormat.parse(date);
	}
	
	public static String parseServletDateFormat(Date date){
		return servletDateFormat.format(date); 
	}
	
	public static String generateString(int length) {

		Random random = new Random();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(AB.charAt(random.nextInt(AB.length())));
		}

		return sb.toString();
	}

	public static String hash256(String base) {
		
		String hashedString = null;
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuilder hexString = new StringBuilder();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			hashedString = hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashedString;
	}

	public static long toUnixSeconds(Date date) {
		return (long) date.getTime() / 1000;
	}

	public static Map<String, String> getParameters(HttpServletRequest servletRequest, String... parameters)
			throws ServletException {

		Map<String, String> requestParameters = new HashMap<String, String>();
		List<String> missingParameters = new ArrayList<String>();

		for (String parameter : parameters) {
			String value = servletRequest.getParameter(parameter);
			if (value == null) {
				missingParameters.add(parameter);
			}
			requestParameters.put(parameter, value);
		}

		if (!missingParameters.isEmpty()) {
			throw new ServletException("BAD REQUEST: MISSING " + missingParameters.toString() + " IN HEADER");
		}

		return requestParameters;
	}

	public static String convertStreamToString(InputStream inputStream) throws IOException {
		return IOUtils.toString(inputStream, "UTF-8");
	}
}
