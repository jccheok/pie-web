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
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewOpenGroupsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8816971244257749858L;
	
	GroupService groupService;
	SchoolService schoolService;
	StaffService staffService;
	StaffGroupService staffGroupService;

	@Inject
	public ViewOpenGroupsServlet(GroupService groupService, SchoolService schoolService, StaffService staffService, StaffGroupService staffGroupService) {
		this.groupService = groupService;
		this.schoolService = schoolService;
		this.staffService = staffService;
		this.staffGroupService = staffGroupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int schoolID = 0;
		int schoolMemberID = 0;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "schoolID", "schoolMemberID");
			schoolID = Integer.parseInt(requestParameters.get("schoolID"));
			schoolMemberID = Integer.parseInt(requestParameters.get("schoolMemberID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		schoolService = new SchoolService();
		
		JSONObject responseObject = new JSONObject();
		
		JSONArray groupList = new JSONArray();
		for (Group openGroup : schoolService.getSchoolOpenValidGroups(schoolID)) {
			
			int groupID = openGroup.getGroupID();
			
			JSONObject groupDetails = new JSONObject();
			groupDetails.put("groupID", groupID);
			groupDetails.put("groupName", openGroup.getGroupName());
			groupDetails.put("groupTypeName", openGroup.getGroupType().toString());
			groupDetails.put("groupDescription", openGroup.getGroupDescription());
			groupDetails.put("groupMemberCount", groupService.getMemberCount(openGroup.getGroupID()));
			groupDetails.put("groupIsPasswordProtected", !openGroup.getGroupCode().equals("NONE"));
			groupDetails.put("isGroupMember", groupService.hasGroupMember(groupID, schoolMemberID));
		
			JSONArray adminList = new JSONArray();
			for (Staff adminStaff : staffGroupService.getGroupAdministrators(groupID)) {
			
				JSONObject adminDetails = new JSONObject();
				adminDetails.put("staffFullName", adminStaff.getUserFullName());
				adminDetails.put("staffGroupRole", staffGroupService.getStaffRole(adminStaff.getUserID(), openGroup.getGroupID()).getStaffRoleName());
				adminList.put(adminDetails);
			}
			
			groupDetails.put("groupAdministrators", adminList);
			groupDetails.put("groupOwner", staffGroupService.getGroupOwner(openGroup.getGroupID()).getUserFullName());
			groupList.put(groupDetails);
		}
		responseObject.put("groupList", groupList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
