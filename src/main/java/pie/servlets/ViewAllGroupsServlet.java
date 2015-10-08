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
import pie.services.GroupService;
import pie.services.SchoolService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewAllGroupsServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7906984824987671886L;
	
	StaffService staffService;
	GroupService groupService;
	SchoolService schoolService;

	@Inject
	public ViewAllGroupsServlet(GroupService groupService, StaffService staffService, SchoolService schoolService) {
		this.groupService = groupService;
		this.staffService = staffService;
		this.schoolService = schoolService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		int schoolID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "schoolID");
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			schoolID = Integer.parseInt(requestParameters.get("schoolID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		} 
		
		JSONObject responseObject = new JSONObject();
		
	//	JSONArray joinedGroups = new JSONArray();
	//	JSONArray notJoinedGroups = new JSONArray();
		
		JSONArray groups = new JSONArray();
		for(Group group : schoolService.getSchoolValidGroups(schoolID)){
			
			int groupID = group.getGroupID();
			
			JSONObject groupDetails = new JSONObject();
			groupDetails.put("groupID", groupID);
			groupDetails.put("groupName", group.getGroupName());
			groupDetails.put("groupDescription", group.getGroupDescription());
			groupDetails.put("groupMemberCount", groupService.getMemberCount(group.getGroupID()));
			
			groupDetails.put("groupOwner",groupService.getGroupOwner(groupID));
//			JSONArray groupAdministrators = new JSONArray();
//			for(Staff adminStaff : groupService.getGroup(groupID)){
//				
//				JSONObject adminDetails = new JSONObject();
//				adminDetails.put("staffFullName", adminStaff.getUserFullName());
//				adminDetails.put("staffGroupRole", staffService.getStaffRole(adminStaff.getUserID(), group.getGroupID()).getStaffRoleName());
//				groupAdministrators.put(adminDetails);
//			}
//			groupDetails.put("groupAdministrators", groupAdministrators);
//							
			groupDetails.put("staffGroupStatus", staffService.isMember(staffID, groupID));
			
			groups.put(groupDetails);
		}
		
//		responseObject.put("joinedGroups", joinedGroups);
//		responseObject.put("notJoinedGroups", notJoinedGroups);
		
		responseObject.put("groups", groups);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
