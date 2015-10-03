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
import pie.constants.JoinGroupResult;
import pie.services.GroupService;
import pie.services.ParentService;
import pie.services.PushNotificationService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AddChildToGroupServlet extends HttpServlet {

	private static final long serialVersionUID = -1179823325679619822L;

	StudentService studentService;
	PushNotificationService pushNotificationService;
	ParentService parentService;
	GroupService groupService;

	@Inject
	public AddChildToGroupServlet(StudentService studentService, PushNotificationService pushNotificationService,
			ParentService parentService, GroupService groupService) {
		this.studentService = studentService;
		this.pushNotificationService = pushNotificationService;
		this.parentService = parentService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int parentID = 0;
		int groupID = 0;
		int studentID = 0;
		String groupCode = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "parentID", "groupID",
					"studentID", "groupCode");
			parentID = Integer.parseInt(requestParameters.get("parentID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			studentID = Integer.parseInt(requestParameters.get("studentID"));
			groupCode = requestParameters.get("groupCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JoinGroupResult joinGroupResult = studentService.joinGroup(groupID, studentID, groupCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());
		
		if (joinGroupResult == JoinGroupResult.SUCCESS) {
			
			String studentIonicUserID = pushNotificationService.getIonicUserID(studentID);
			Parent parent = parentService.getParent(parentID);
			Group group = groupService.getGroup(groupService.getGroupID(groupCode));
			String message = parent.getUserFullName() + " added you to " + group.getGroupName();
			pushNotificationService.sendNotification(message, studentIonicUserID);
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
