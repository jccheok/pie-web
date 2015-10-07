package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.constants.ResetPasswordResult;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class ResetPasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 8828191475435776429L;
	
	UserService userService;
	
	@Inject
	public ResetPasswordServlet(UserService userService) {
		this.userService = userService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String securityQuestionAnswer = null;
		int userID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "securityQuestionAnswer", "userID");
			securityQuestionAnswer = requestParameters.get("securityQuestionAnswer");
			userID = Integer.parseInt(requestParameters.get("userID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		ResetPasswordResult resetPasswordResult = userService.resetPassword(userID, securityQuestionAnswer, this);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", resetPasswordResult.toString());
		responseObject.put("message", resetPasswordResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}



}
