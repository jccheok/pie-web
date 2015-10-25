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
import pie.StaffRole;
import pie.constants.JoinGroupResult;
import pie.services.EmailService;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StaffRoleService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StaffJoinGroupServlet extends HttpServlet {

	private static final long serialVersionUID = 5666243950594572419L;

	StaffService staffService;
	GroupService groupService;
	StaffRoleService staffRoleService;
	EmailService emailService;
	StaffGroupService staffGroupService;

	@Inject
	public StaffJoinGroupServlet(StaffService staffService, GroupService groupService, StaffRoleService staffRoleService, EmailService emailService, StaffGroupService staffGroupService) {
		this.staffService = staffService;
		this.groupService = groupService;
		this.staffRoleService = staffRoleService;
		this.emailService = emailService;
		this.staffGroupService = staffGroupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupID = 0;
		int staffID = 0;
		int staffRoleID = 0;
		String groupCode = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "staffID",
					"staffRoleID", "groupCode");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			staffRoleID = Integer.parseInt(requestParameters.get("staffRoleID"));
			groupCode = requestParameters.get("groupCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		StaffRole staffRole = staffRoleService.getStaffRole(staffRoleID);

		JoinGroupResult joinGroupResult = staffGroupService.joinGroup(groupID, staffID, groupCode, staffRole);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		if (joinGroupResult == JoinGroupResult.SUCCESS) {
			
			String staffFullname = staffService.getStaff(staffID).getUserFullName();
			String groupName = groupService.getGroup(groupID).getGroupName();
			String emailSubject = staffFullname + " has joined " + groupName;
			
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
