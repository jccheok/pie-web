package pie.servlets.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class VerifyUserServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4253371369355408839L;
	
	UserService userService;
	
	@Inject
	public VerifyUserServlet(UserService userService) {
		this.userService = userService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userID");
			userID = Integer.parseInt(requestParameters.get("userID"));
			
		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		User user = userService.getUser(userID);
		
		boolean verifyResult = userService.verifyUser(user.getUserEmail());
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", verifyResult);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}