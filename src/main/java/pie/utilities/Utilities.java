package pie.utilities;

import java.util.Random;

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
}
