package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Group;
import pie.StaffRole;
import pie.StudentGroup;
import pie.User;
import pie.UserType;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StudentGroupService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetGroupDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = -288687819678490074L;
	
	GroupService groupService;
	UserService userService;
	StaffGroupService staffGroupService;
	StudentGroupService studentGroupService;
	
	@Inject
	public GetGroupDetailsServlet(GroupService groupService, UserService userService, StaffGroupService staffGroupService, StudentGroupService studentGroupService) {
		this.groupService = groupService;
		this.userService = userService;
		this.staffGroupService = staffGroupService;
		this.studentGroupService = studentGroupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int groupID = 0;
		int userID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "userID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			userID = Integer.parseInt(requestParameters.get("userID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Group group = groupService.getGroup(groupID);

		JSONObject responseObject = new JSONObject();

		responseObject.put("groupID", group.getGroupID());
		responseObject.put("groupType", group.getGroupType().toString());
		responseObject.put("groupName", group.getGroupName());
		responseObject.put("groupCode", group.getGroupCode());
		responseObject.put("isCodeProtected", group.getGroupCode().equals("NONE") ? false: true);
		responseObject.put("groupDescription", group.getGroupDescription());
		responseObject.put("groupMaxDailyHomeworkMinutes", group.getGroupMaxDailyHomeworkMinutes());
		responseObject.put("groupEndDate", Utilities.parseServletDateFormat(group.getExpiryDate()));
		
		User user = userService.getUser(userID);
		if(user.getUserType() == UserType.STAFF){
			StaffRole staffRole = staffGroupService.getStaffRole(userID, groupID);
			if(staffRole != null){
				responseObject.put("userRole", staffRole.getStaffRoleName());
				responseObject.put("userID", userID);
			}else{
				responseObject.put("userID", userID);
				responseObject.put("isMember", staffGroupService.hasGroupMember(userID, groupID));
			}
			
		}else if(user.getUserType() == UserType.STUDENT){
			StudentGroup studentGroup = studentGroupService.getStudentGroup(groupID, userID);
			if(studentGroup != null){
				responseObject.put("studentJoinDate", Utilities.parseServletDateFormat(studentGroup.getJoinDate()));
				responseObject.put("indexNumber", studentGroup.getIndexNumber());
				responseObject.put("studentGroupID", studentGroup.getStudentGroupID());
			}
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
