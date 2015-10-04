package pie.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.User;

public class PushNotificationService {
	
	String IONIC_URL = "https://push.ionic.io/api/v1/push";
	String APP_ID = "0b491af9";
	String PRIVATE_API_KEY = "242c157df90683f16d6dacd6a1bf77e60726032ff2475501";
	
	public String getIonicUserID(int userID) {
		
		UserService userService = new UserService();
		
		User user = userService.getUser(userID);
		return userID + '@' + user.getUserFirstName() + user.getUserLastName();
	}
	
	public boolean sendNotification(String message, String ionicUserID) {
		
		boolean sendResult = false;
		
		try {
			
			JSONObject requestData = prepareIonicPushData(message, ionicUserID);
			
			URL url = new URL(IONIC_URL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("X-Ionic-Application-Id", APP_ID);
			connection.setRequestProperty("Authorization", "Basic " + PRIVATE_API_KEY);
			
			OutputStreamWriter outputWriter = new OutputStreamWriter(connection.getOutputStream());
			outputWriter.write(requestData.toString());
			outputWriter.flush();
			outputWriter.close();
			
			StringBuilder response = new StringBuilder();
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = inputReader.readLine()) != null) {
				response.append(line);
			}
			inputReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sendResult;
	}
	
	private JSONObject prepareIonicPushData(String message, String... ionicUserIDs) {
		
		JSONObject requestData = new JSONObject();
		
		JSONArray recipientsData = new JSONArray();
		for (String ionicUserID : ionicUserIDs) {
			recipientsData.put(ionicUserID);
		}
		requestData.put("user_ids", ionicUserIDs);
		
		JSONObject notificationData = new JSONObject().put("alert", message);
		requestData.put("notification", notificationData);
		
		return requestData;
	}

}
