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
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterStudentServlet extends HttpServlet {

	private static final long serialVersionUID = -7966834264489316794L;

	StudentService studentService;

	@Inject
	public RegisterStudentServlet(StudentService studentService) {
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userEmail = null;
		String userPassword = null;
		String userMobile = null;
		String studentCode = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userEmail", "userPassword",
					"userMobile", "studentCode");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			userMobile = requestParameters.get("userMobile");
			studentCode = requestParameters.get("studentCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		UserRegistrationResult registrationResult = studentService.registerStudent(userEmail, userPassword, userMobile,
				studentCode);

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