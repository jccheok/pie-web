package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UserRegistrationResult;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterTeacherServlet extends HttpServlet {

	private static final long serialVersionUID = 8776151424113551105L;

	StaffService teacherService;

	@Inject
	public RegisterTeacherServlet(StaffService teacherService) {
		this.teacherService = teacherService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userFirstName = null;
		String userLastName = null;
		String userEmail = null;
		String userPassword = null;
		String userMobile = null;
		String schoolCode = null;
		String teacherTitle = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userFirstName", "userLastName",
					"userEmail", "userPassword", "userMobile", "schoolCode", "teacherTitle");
			userFirstName = requestParameters.get("userFirstName");
			userLastName = requestParameters.get("userLastName");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			userMobile = requestParameters.get("userMobile");
			schoolCode = requestParameters.get("schoolCode");
			teacherTitle = requestParameters.get("teacherTitle");

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		UserRegistrationResult registrationResult = teacherService.registerStaff(userFirstName, userLastName,
				userEmail, userPassword, userMobile, schoolCode, teacherTitle);

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