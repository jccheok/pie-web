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

import pie.User;
import pie.constants.ResetPasswordResult;
import pie.services.EmailService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResetPasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 8828191475435776429L;
	
	UserService userService;
	EmailService emailService;
	
	@Inject
	public ResetPasswordServlet(UserService userService, EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
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
		
		User user = userService.getUser(userID);
		
		String newPassword = Utilities.generateString(10);
		String hashedNewPassword = Utilities.hash256(newPassword);
		
		while (hashedNewPassword.equals(user.getUserPassword())) {
			newPassword = Utilities.generateString(10);
			hashedNewPassword = Utilities.hash256(newPassword);
		}
				
		ResetPasswordResult resetPasswordResult = userService.resetPassword(userID, securityQuestionAnswer, hashedNewPassword);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("result", resetPasswordResult.toString());
		responseObject.put("message", resetPasswordResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		if (resetPasswordResult == ResetPasswordResult.SUCCESS) {
				
			InputStream emailTemplateStream = getServletContext().getResourceAsStream("/resources/resetPasswordTemplate.html");
			
			String emailSubject = "Password Reset on PETAL";
			String emailTemplate = Utilities.convertStreamToString(emailTemplateStream);

			String emailContent = emailTemplate.replaceAll("\\$FIRST_NAME", user.getUserFirstName());
			emailContent = emailContent.replaceAll("\\$PASSWORD", newPassword);
			
			emailService.sendEmail(emailSubject, emailContent, new String[] { user.getUserEmail() });
		}
	}



}
