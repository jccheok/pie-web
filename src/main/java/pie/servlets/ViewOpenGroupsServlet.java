package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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

	@Inject
	public ViewOpenGroupsServlet(GroupService groupService, SchoolService schoolService) {
		this.groupService = groupService;
		this.schoolService = schoolService;
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
		Group[] groups = schoolService.getSchoolOpenValidGroups(schoolID);
		
		JSONObject responseObject = new JSONObject();
		JSONArray groupList = new JSONArray();

		
		for (Group group : groups) {
			HashMap<String, String> currGroup = new HashMap<String, String>();
			currGroup.put("groupName", group.getGroupName());
			currGroup.put("groupDescription", group.getGroupDescription());
			currGroup.put("groupMemberCount", Integer.toString(groupService.getMemberCount(group.getGroupID())));
		
			JSONArray adminList = new JSONArray();
			
			Staff[] adminStaff = groupService.getGroupAdministrators(group.getGroupID());
			
			for (Staff staff : adminStaff) {
			
				HashMap<String, String> staffAdmin = new HashMap<String, String>();
				staffAdmin.put("adminName", staff.getUserFullName());
				adminList.put(staffAdmin);
				
			}
			
			currGroup.put("groupAdministrators", adminList.toString());
			groupList.put(currGroup);
		}
		responseObject.put("groupList", groupList);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
