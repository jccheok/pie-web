package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.GroupService;
import pie.services.SchoolService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GenerateCodeServlet extends HttpServlet {

	private static final long serialVersionUID = 392452330560754644L;
	
	StudentService studentService;
	SchoolService schoolService;
	GroupService groupService;

	@Inject
	public GenerateCodeServlet(StudentService studentService, SchoolService schoolService, GroupService groupService) {
		this.studentService = studentService;
		this.schoolService = schoolService;
		this.groupService = groupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String passwordType = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "passwordType");
			passwordType = requestParameters.get("passwordType");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		String newPassword = null;
		
		if (passwordType.equals("student")) {
			newPassword = studentService.generateStudentCode();
		} else if (passwordType.equals("group")) {
			newPassword = groupService.generateGroupCode();
		} else if (passwordType.equals("school")) {
			newPassword = schoolService.generateSchoolCode();
		}

		JSONObject responseObject = new JSONObject();
		responseObject.put("newCode", newPassword);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
