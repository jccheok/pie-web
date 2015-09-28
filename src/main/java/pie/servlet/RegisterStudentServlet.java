package pie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.service.StudentService;
import pie.service.StudentService.RegistrationResult;

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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String userEmail = request.getParameter("userEmail");
		String userPassword = request.getParameter("userPassword");
		String userMobile = request.getParameter("userMobile");
		String studentCode = request.getParameter("studentCode");
		
		RegistrationResult registrationResult = studentService.registerStudent(userEmail, userPassword, userMobile, studentCode);
		
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