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
import pie.services.AuthService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UpdateUserAccountDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 1001623595677723664L;

	UserService userService;
	AuthService authService;

	@Inject
	public UpdateUserAccountDetailsServlet(UserService userService, AuthService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int userID = 0;
		String userMobile = null;
		int securityQuestionID = 0;
		String securityQuestionAnswer = null;
		String addressStreet = null;
		String addressPostalCode = null;
		int cityID = 0;
		String authToken = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userID", "userMobile", "securityQuestionID", "securityQuestionAnswer", "addressStreet",
					"addressPostalCode", "cityID", "authToken");

			userID = Integer.parseInt(requestParameters.get("userID"));
			
			userMobile = requestParameters.get("userMobile");
			securityQuestionID = Integer.parseInt(requestParameters.get("securityQuestionID"));
			securityQuestionAnswer = requestParameters.get("securityQuestionAnswer");
			addressStreet = requestParameters.get("addressStreet");
			addressPostalCode = requestParameters.get("addressPostalCode");
			cityID = Integer.parseInt(requestParameters.get("cityID"));
			authToken = requestParameters.get("authToken");

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();

		UpdateAccountResult updateAccountResult = userService.updateUserAccountDetails(userID, userMobile, securityQuestionID, securityQuestionAnswer, addressStreet,
				addressPostalCode, cityID, authToken);
		responseObject.put("result", updateAccountResult.toString());
		responseObject.put("message", updateAccountResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}
