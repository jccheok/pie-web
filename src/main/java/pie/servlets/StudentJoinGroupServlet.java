package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.JoinGroupResult;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StudentJoinGroupServlet extends HttpServlet {

	private static final long serialVersionUID = 1354541147333762368L;
	
	StudentService studentService;

	@Inject
	public StudentJoinGroupServlet(StudentService studentService) {
		this.studentService = studentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int groupID = 0;
		int studentID = 0;
		String groupCode = null;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "studentID",
					"groupCode");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			studentID = Integer.parseInt(requestParameters.get("studentID"));
			groupCode = requestParameters.get("groupCode");
			
		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		JoinGroupResult joinGroupResult = studentService.joinGroup(groupID, studentID, groupCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
