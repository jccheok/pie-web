package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.LeaveGroupResult;
import pie.services.GroupService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class StudentLeaveGroupServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1120142003759961147L;
	
	StudentService studentService;
	GroupService groupService;
	
	@Inject
	public StudentLeaveGroupServlet(StudentService studentService, GroupService groupService) {
		this.studentService = studentService;
		this.groupService = groupService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int groupID = 0;
		int studentID = 0;

		try {
	
			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "studentID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			studentID = Integer.parseInt(requestParameters.get("studentID"));
			
		} catch (Exception e) {
	
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
	
		LeaveGroupResult leaveGroupResult = studentService.leaveGroup(studentID, groupID);
	
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", leaveGroupResult.toString());
		responseObject.put("message", leaveGroupResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
	
}

