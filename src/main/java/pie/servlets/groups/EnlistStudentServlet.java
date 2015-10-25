package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EnlistStudentServlet extends HttpServlet {

	private static final long serialVersionUID = 135775505821984554L;
	
	StudentService studentService;
	
	@Inject
	public EnlistStudentServlet(StudentService studentService){
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String studentFirstName = null;
		String studentLastName = null;
		int studentIndexNumber = -1;
		int groupID = -1;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentFirstName", "studentLastName", "studentIndexNumber", "groupID");
			studentFirstName = requestParameters.get("studentFirstName");
			studentLastName = requestParameters.get("studentLastName");
			studentIndexNumber = Integer.parseInt(requestParameters.get("studentIndexNumber"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		String studentCode = studentService.generateStudentCode();
		
		studentService.enlistStudent(studentFirstName, studentLastName, studentCode, groupID, studentIndexNumber);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", GenericResult.SUCCESS.toString());
		responseObject.put("message", "Successfully enlisted new student!");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
}
