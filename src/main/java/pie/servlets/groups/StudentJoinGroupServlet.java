package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Staff;
import pie.constants.JoinGroupResult;
import pie.services.EmailService;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StudentJoinGroupServlet extends HttpServlet {

	private static final long serialVersionUID = 1354541147333762368L;
	
	StudentService studentService;
	GroupService groupService;
	EmailService emailService;
	StaffGroupService staffGroupService;

	@Inject
	public StudentJoinGroupServlet(StudentService studentService, GroupService groupService, EmailService emailService, StaffGroupService staffGroupService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.emailService = emailService;
		this.staffGroupService = staffGroupService;
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
			return;
		}

		JoinGroupResult joinGroupResult = studentService.joinGroup(studentID, groupID, groupCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		if (joinGroupResult == JoinGroupResult.SUCCESS) {
			
			String studentFullName = studentService.getStudent(studentID).getUserFullName();
			String groupName = groupService.getGroup(groupID).getGroupName();
			String emailSubject = studentFullName + " has joined " + groupName;
			
			String emailContent = emailSubject;
			
			String[] groupAdministratorsEmail = {};
			
			Staff[] groupAdministrators = staffGroupService.getGroupAdministrators(groupID);
			List<String> tempGroupAdministratorsEmail = new ArrayList<String>(); 
			for (Staff groupAdmin : groupAdministrators) {
				tempGroupAdministratorsEmail.add(groupAdmin.getUserEmail());
			}
			groupAdministratorsEmail = tempGroupAdministratorsEmail.toArray(groupAdministratorsEmail);
			
			emailService.sendEmail(emailSubject, emailContent, groupAdministratorsEmail);
		}
		
	}

}
