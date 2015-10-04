package pie.servlets;

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
import pie.services.StaffService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterStaffServlet extends HttpServlet {

	private static final long serialVersionUID = 6380932722577144623L;
	
	StaffService staffService;
	UserService userService;
	EmailService emailService;

	@Inject
	public RegisterStaffServlet(StaffService staffService, EmailService emailService, UserService userService) {
		this.staffService = staffService;
		this.userService = userService;
		this.emailService = emailService;
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
			return;
		}

		UserRegistrationResult registrationResult = staffService.registerStaff(userFirstName, userLastName,
				userEmail, userPassword, userMobile, schoolCode, teacherTitle);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		if (registrationResult == UserRegistrationResult.SUCCESS) {

			String verificationLink = "http://piedev-rpmaps.rhcloud.com/servlets/verify?userID=" + userService.getUserID(userEmail);
			InputStream emailTemplateStream = this.getServletContext().getResourceAsStream("/resources/verificationTemplate.html");
			
			String emailSubject = "Confirm your Staff account on Partners in Education";
			String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);
			
			String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", userFirstName);
			emailContent = emailContent.replaceAll("\\$VERIFICATION_LINK", verificationLink);
			
			emailService.sendEmail(emailSubject, emailContent, new String[] {userEmail});
		}

	}
}