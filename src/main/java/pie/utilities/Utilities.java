package pie.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class Utilities {
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String generateString(int length) {
		
		Random random = new Random();

		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			sb.append(AB.charAt(random.nextInt(AB.length())));
		}

		return sb.toString();
	}
	
	public static Map<String, String> getParameters(HttpServletRequest servletRequest, String... parameters) throws ServletException {
		
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
