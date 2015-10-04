package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.GroupService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EnlistStudentsToGroupServlet extends HttpServlet {
	
	private static final long serialVersionUID = -1810174238170625377L;
	
	StudentService studentService;
	GroupService groupService;
	
	@Inject
	public EnlistStudentsToGroupServlet(StudentService studentService, GroupService groupService){
		this.studentService = studentService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String rawStudentList = null;
		int groupID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentList", "groupID");
			rawStudentList = requestParameters.get("studentsData");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		JSONObject requestObject = new JSONObject(rawStudentList);
		JSONArray studentList = requestObject.getJSONArray("studentList");
		
		int schoolID = groupService.getGroup(groupID).getSchool().getSchoolID();
		
		for (int index = 0; index < studentList.length(); index++) {
			
			JSONObject student = studentList.getJSONObject(index);
			
			String studentFirstName = student.getString("studentFirstName");
			String studentLastName = student.getString("studentLastName");
			String studentCode = studentService.generateStudentCode();
			int studentGroupIndexNumber = Integer.parseInt(student.getString("studentGroupIndexNumber"));
			
			studentService.enlistStudent(studentFirstName, studentLastName, studentCode, schoolID, groupID, studentGroupIndexNumber);
		}
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", GenericResult.SUCCESS.toString());
		responseObject.put("message", "Successfully enlisted all students!");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
