package pie.servlets.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.UserRegistrationResult;
import pie.services.EmailService;
import pie.services.StudentService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterStudentServlet extends HttpServlet {

	private static final long serialVersionUID = -7966834264489316794L;

	StudentService studentService;
	UserService userService;
	EmailService emailService;

	@Inject
	public RegisterStudentServlet(StudentService studentService, UserService userService, EmailService emailService) {
		this.studentService = studentService;
		this.userService = userService;
		this.emailService = emailService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userEmail = null;
		String userPassword = null;
		String userMobile = null;
		String studentCode = null;
		int securityQuestionID = 0;
		String securityQuestionAnswer = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userEmail", "userPassword",
					"userMobile", "studentCode", "securityQuestionID", "securityQuestionAnswer");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			userMobile = requestParameters.get("userMobile");
			studentCode = requestParameters.get("studentCode");
			securityQuestionID = Integer.parseInt(requestParameters.get("securityQuestionID"));
			securityQuestionAnswer = requestParameters.get("securityQuestionAnswer");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserRegistrationResult registrationResult = studentService.registerStudent(userEmail, userPassword, userMobile,
				studentCode, securityQuestionID, securityQuestionAnswer);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

		if (registrationResult == UserRegistrationResult.SUCCESS) {

			int userID = userService.getUserID(userEmail);

			String verificationLink = "http://piedev-rpmaps.rhcloud.com/servlets/verify?userID=" + userID;
			InputStream emailTemplateStream = this.getServletContext().getResourceAsStream("/resources/verificationTemplate.html");

			String emailSubject = "Confirm your Student account on PETAL";
			String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);

			String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", userService.getUser(userID).getUserFirstName());
			emailContent = emailContent.replaceAll("\\$VERIFICATION_LINK", verificationLink);

			emailService.sendEmail(emailSubject, emailContent, new String[] { userEmail });
		}

	}
}