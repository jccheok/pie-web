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
public class EnlistStudentsByBatchServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3674377035466826694L;
	
	StudentService studentService;
	StudentGroupService studentGroupService;
	GroupService groupService;
	
	@Inject
	public EnlistStudentsByBatchServlet(StudentService studentService, StudentGroupService studentGroupService, GroupService groupService){
		this.studentService = studentService;
		this.studentGroupService = studentGroupService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String studentsData = null;
		int groupID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentsData", "groupID");
			studentsData = requestParameters.get("studentsData");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		JSONObject requestObject = new JSONObject(studentsData);
		JSONArray studentList = requestObject.getJSONArray("studentList");
		
		GroupType groupType = groupService.getGroup(groupID).getGroupType();
		
		boolean enlistResult = false;
		if(groupType == GroupType.HOME ){
			enlistResult = studentService.enlistStudent(studentList, groupID);
		}
		
//		for (int index = 0; index < studentList.length(); index++) {
//			
//			JSONObject studentObject = studentList.getJSONObject(index);
//			
//			String studentFirstName = studentObject.getString("studentFirstName");
//			String studentLastName = studentObject.getString("studentLastName");
//			int studentIndexNumber = studentObject.getInt("studentGroupIndexNumber");
//			String SUID = studentObject.getString("SUID");
//			
//			Student student = studentService.studentExists(SUID);
//			if(student == null && groupType == GroupType.HOME){
//				String studentCode = studentService.generateStudentCode();
//				studentService.enlistStudent(studentFirstName, studentLastName, studentCode, groupID, studentIndexNumber, SUID);
//			}else if(student != null){
//				studentGroupService.addStudentToGroup(groupID, student.getUserID(), studentIndexNumber);	
//			}
//		}
//		
		JSONObject responseObject = new JSONObject();
		
		if(enlistResult){
			responseObject.put("result", GenericResult.SUCCESS.toString());
			responseObject.put("message", "Students successfuly enlisted.");
		}else{
			responseObject.put("result", GenericResult.FAILED.toString());
			responseObject.put("message", "Students failed to be enlisted.");
		}
		

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
