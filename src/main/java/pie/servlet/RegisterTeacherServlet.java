package pie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.service.TeacherService.RegistrationResult;
import pie.service.TeacherService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterTeacherServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8776151424113551105L;
	
	TeacherService teacherService;

	@Inject
	public RegisterTeacherServlet(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String userFirstName = request.getParameter("userFirstName");
		String userLastName = request.getParameter("userLastName");
		String userEmail = request.getParameter("userEmail");
		String userPassword = request.getParameter("userPassword");
		String userMobile = request.getParameter("userMobile");
		String studentCode = request.getParameter("schoolCode");
		String teacherTitle = request.getParameter("teacherTitle");
		
		RegistrationResult registrationResult = teacherService.registerTeacher(userFirstName, userLastName, userEmail, userPassword, userMobile, studentCode, teacherTitle);
		
		JSONObject responseObject = new JSONObject();
		
		for (RegistrationResult result : RegistrationResult.values()) {
			if (result == registrationResult) {
				
				responseObject.put("result", result.toString());
				responseObject.put("message", result.getDefaultMessage());
				break;
			}
		}
		
		if (registrationResult == RegistrationResult.SUCCESS) {

			// send email
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}