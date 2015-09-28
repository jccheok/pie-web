package pie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.service.StaffService.RegistrationResult;
import pie.service.StaffService;

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
		
		RegistrationResult registrationResult = teacherService.registerStaff(userFirstName, userLastName, userEmail, userPassword, userMobile, studentCode, teacherTitle);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());
		
		if (registrationResult == RegistrationResult.SUCCESS) {

			// send email
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}