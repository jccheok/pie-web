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

import pie.Group;
import pie.Staff;
import pie.services.GroupService;
import pie.services.SchoolService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewStaffJoinedGroupsServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7906984824987671886L;
	
	StaffService staffService;
	GroupService groupService;
	SchoolService schoolService;

	@Inject
	public ViewStaffJoinedGroupsServlet(GroupService groupService, StaffService staffService, SchoolService schoolService) {
		this.groupService = groupService;
		this.staffService = staffService;
		this.schoolService = schoolService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID");
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		} 
		
		JSONObject responseObject = new JSONObject();
		
		JSONArray joinedGroups = new JSONArray();
		for(Group group : staffService.getJoinedGroups(staffID)){
			
			int groupID = group.getGroupID();
			
			JSONObject groupDetails = new JSONObject();
			groupDetails.put("groupID", groupID);
			groupDetails.put("groupName", group.getGroupName());
			groupDetails.put("groupDescription", group.getGroupDescription());
			groupDetails.put("groupMemberCount", groupService.getMemberCount(group.getGroupID()));
			
			JSONArray groupAdministrators = new JSONArray();
			for(Staff adminStaff : groupService.getGroupAdministrators(groupID)){
				
				JSONObject adminDetails = new JSONObject();
				adminDetails.put("staffFullName", adminStaff.getUserFullName());
				adminDetails.put("staffGroupRole", staffService.getStaffRole(adminStaff.getUserID(), group.getGroupID()).getStaffRoleName());
				groupAdministrators.put(adminDetails);
			}
			groupDetails.put("groupAdministrators", groupAdministrators);
			joinedGroups.put(groupDetails);
		}
		
		responseObject.put("joinedGroups", joinedGroups);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
