package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Group;
import pie.GroupType;
import pie.Student;
import pie.constants.GenericResult;
import pie.services.GroupService;
import pie.services.StudentGroupService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EnlistStudentServlet extends HttpServlet {

	private static final long serialVersionUID = 135775505821984554L;
	
	StudentService studentService;
	GroupService groupService;
	StudentGroupService studentGroupService;
	
	@Inject
	public EnlistStudentServlet(StudentService studentService, GroupService groupService, StudentGroupService studentGroupService){
		this.studentService = studentService;
		this.groupService = groupService;
		this.studentGroupService = studentGroupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String studentFirstName = null;
		String studentLastName = null;
		int studentIndexNumber = -1;
		String SUID = null;
		int groupID = -1;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentFirstName", "studentLastName", "studentIndexNumber", "groupID", "SUID");
			studentFirstName = requestParameters.get("studentFirstName");
			studentLastName = requestParameters.get("studentLastName");
			studentIndexNumber = Integer.parseInt(requestParameters.get("studentIndexNumber"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			SUID = requestParameters.get("SUID");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		Group group = groupService.getGroup(groupID);
		GroupType groupType = group.getGroupType();
		Student student = studentService.studentExists(SUID);
		JSONObject responseObject = new JSONObject();

		responseObject.put("result", GenericResult.SUCCESS.toString());
		responseObject.put("message", "Successfully added student to group.");
		
		JSONArray newStudent = new JSONArray();
		JSONObject studentObject = new JSONObject();
		
		
		studentObject.put("studentFirstName", studentFirstName);
		studentObject.put("studentLastName", studentLastName);
		studentObject.put("studentGroupIndexNumber", studentIndexNumber);
		newStudent.put(studentObject);
		
		if(student == null && groupType == GroupType.HOME){
			studentService.enlistStudent(newStudent, groupID);
		}else if(student != null){
			studentGroupService.addStudentToGroup(groupID, newStudent);
		}else{
			responseObject.put("result", GenericResult.FAILED.toString());
			responseObject.put("message", "Register user first");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
}
