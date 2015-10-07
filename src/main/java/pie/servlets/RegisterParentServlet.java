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
import pie.services.ParentService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterParentServlet extends HttpServlet {

	private static final long serialVersionUID = 6025770482395154507L;

	ParentService parentService;
	UserService userService;
	EmailService emailService;

	@Inject
	public RegisterParentServlet(ParentService parentService, UserService userService, EmailService emailService) {
		this.parentService = parentService;
		this.userService = userService;
		this.emailService = emailService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String userFirstName = null;
		String userLastName = null;
		String userEmail = null;
		String userPassword = null;
		String userMobile = null;
		int securityQuestionID = 0;
		String securityQuestionAnswer = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userFirstName", "userLastName", "userEmail", "userPassword", "userMobile", "securityQuestionID", "securityQuestionAnswer");
			userFirstName = requestParameters.get("userFirstName");
			userLastName = requestParameters.get("userLastName");
			userEmail = requestParameters.get("userEmail");
			userPassword = requestParameters.get("userPassword");
			userMobile = requestParameters.get("userMobile");
			securityQuestionID = Integer.parseInt(requestParameters.get("securityQuestionID"));
			securityQuestionAnswer = requestParameters.get("securityQuestionAnswer");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserRegistrationResult registrationResult = parentService.registerParent(userFirstName, userLastName, userEmail, userPassword, userMobile, securityQuestionID, securityQuestionAnswer);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		if (registrationResult == UserRegistrationResult.SUCCESS) {
			
			String verificationLink = "http://piedev-rpmaps.rhcloud.com/servlets/verify?userID=" + userService.getUserID(userEmail);
			InputStream emailTemplateStream = this.getServletContext().getResourceAsStream("/resources/verificationTemplate.html");
			
			String emailSubject = "Confirm your Parent account on Partners in Education";
			String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);
			
			String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", userFirstName);
			emailContent = emailContent.replaceAll("\\$VERIFICATION_LINK", verificationLink);
			
			emailService.sendEmail(emailSubject, emailContent, new String[] {userEmail});
		}
	}
}