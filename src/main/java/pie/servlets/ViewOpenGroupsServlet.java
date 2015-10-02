package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
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
public class ViewOpenGroupsServlet {
	
	GroupService groupService;
	SchoolService schoolService;
	StaffService staffService;

	@Inject
	public ViewOpenGroupsServlet(GroupService groupService, SchoolService schoolService, StaffService staffService) {
		this.groupService = groupService;
		this.schoolService = schoolService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int schoolID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "schoolID");
			schoolID = Integer.parseInt(requestParameters.get("schoolID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			
		}
		
		schoolService = new SchoolService();
		
		JSONObject responseObject = new JSONObject();
		
		JSONArray groupList = new JSONArray();
		for (Group openGroup : schoolService.getSchoolOpenValidGroups(schoolID)) {
			
			int groupID = openGroup.getGroupID();
			
			JSONObject groupDetails = new JSONObject();
			groupDetails.put("groupID", groupID);
			groupDetails.put("groupName", openGroup.getGroupName());
			groupDetails.put("groupDescription", openGroup.getGroupDescription());
			groupDetails.put("groupMemberCount", Integer.toString(groupService.getMemberCount(openGroup.getGroupID())));
			groupDetails.put("groupIsPasswordProtected", openGroup.getGroupCode() == null);
		
			JSONArray adminList = new JSONArray();
			for (Staff adminStaff : groupService.getGroupAdministrators(groupID)) {
			
				JSONObject adminDetails = new JSONObject();
				adminDetails.put("staffFullName", adminStaff.getUserFullName());
				adminDetails.put("staffGroupRole", staffService.getStaffRole(adminStaff.getUserID(), openGroup.getGroupID()).getStaffRoleName());
				adminList.put(adminDetails);
			}
			
			groupDetails.put("groupAdministrators", adminList);
			groupList.put(groupDetails);
		}
		responseObject.put("groupList", groupList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
