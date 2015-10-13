package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UpdateAccountResult;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UpdateUserAccountDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1001623595677723664L;

	UserService userService;
	
	@Inject
	public UpdateUserAccountDetailsServlet(UserService userService) {
		this.userService = userService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;
		String userFirstName = null;
		String userLastName = null;
		String userMobile = null;
		int securityQuestionID = 0;
		String securityQuestionAnswer = null;
		String addressStreet = null;
		String addressPostalCode = null;
		int cityID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userID", "userFirstName", "userLastName", "userMobile", "securityQuestionID", "securityQuestionAnswer", "addressStreet", "addressPostalCode", "cityID");

			userID = Integer.parseInt(requestParameters.get("userID"));
			userFirstName = requestParameters.get("userFirstName");
			userLastName = requestParameters.get("userLastName");
			userMobile = requestParameters.get("userMobile");
			securityQuestionID = Integer.parseInt(requestParameters.get("securityQuestionID"));
			securityQuestionAnswer = requestParameters.get("securityQuestionAnswer");
			addressStreet = requestParameters.get("addressStreet");
			addressPostalCode = requestParameters.get("addressPostalCode");
			cityID = Integer.parseInt(requestParameters.get("cityID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		UpdateAccountResult updateAccountResult = userService.updateUserAccountDetails(userID, userFirstName, userLastName, userMobile, securityQuestionID, securityQuestionAnswer, addressStreet, addressPostalCode, cityID);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", updateAccountResult.toString());
		responseObject.put("message", updateAccountResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
