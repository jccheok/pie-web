package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UserRegistrationResult;
import pie.services.ParentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterParentServlet extends HttpServlet {

	private static final long serialVersionUID = 6025770482395154507L;

	ParentService parentService;

	@Inject
	public RegisterParentServlet(ParentService parentService) {
		this.parentService = parentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String userFirstName = request.getParameter("userFirstName");
		String userLastName = request.getParameter("userLastName");
		String userEmail = request.getParameter("userEmail");
		String userPassword = request.getParameter("userPassword");
		String userMobile = request.getParameter("userMobile");

		UserRegistrationResult registrationResult = parentService.registerParent(userFirstName, userLastName,
				userEmail, userPassword, userMobile);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		if (registrationResult == UserRegistrationResult.SUCCESS) {
			// send email
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}