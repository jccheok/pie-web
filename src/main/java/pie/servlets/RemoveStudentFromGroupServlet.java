package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Group;
import pie.Parent;
import pie.constants.LeaveGroupResult;
import pie.services.GroupService;
import pie.services.ParentService;
import pie.services.PushNotificationService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RemoveStudentFromGroupServlet extends HttpServlet {

	private static final long serialVersionUID = -7726043168449386409L;
	
	StudentService studentService;
	GroupService groupService;
	ParentService parentService;
	PushNotificationService pushNotificationService;
	
	@Inject
	public RemoveStudentFromGroupServlet(StudentService studentService, GroupService groupService,
			ParentService parentService, PushNotificationService pushNotificationService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.parentService = parentService;
		this.pushNotificationService = pushNotificationService;
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int groupID = 0;
		int studentID = 0;
		int parentID = 0;

		try {
	
			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "studentID", "parentID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			studentID = Integer.parseInt(requestParameters.get("studentID"));
			parentID = Integer.parseInt(requestParameters.get("parentID"));
			
		} catch (Exception e) {
	
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
	
		LeaveGroupResult leaveGroupResult = studentService.leaveGroup(studentID, groupID);
	
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", leaveGroupResult.toString());
		responseObject.put("message", leaveGroupResult.getDefaultMessage());
		
		if (leaveGroupResult == LeaveGroupResult.SUCCESS && studentService.getStudent(studentID).userIsVerified()) {
			
			String studentIonicUserID = pushNotificationService.getIonicUserID(studentID);
			Parent parent = parentService.getParent(parentID);
			Group group = groupService.getGroup(groupID);
			String message = parent.getUserFullName() + " removed you from " + group.getGroupName();
			pushNotificationService.sendNotification(message, studentIonicUserID);
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}

}
