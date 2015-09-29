package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.services.UserService;

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

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		int userID = Integer.valueOf(request.getParameter("userID"));
		User user = userService.getUser(userID);
		
		boolean verifyResult = userService.verifyUser(user);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", verifyResult);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}