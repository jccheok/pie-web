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
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewStudentJoinedGroupsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1883856357216052310L;
	
	StudentService studentService;
	GroupService groupService;
	SchoolService schoolService;
	StaffService staffService;

	@Inject
	public ViewStudentJoinedGroupsServlet(GroupService groupService, StudentService studentService, SchoolService schoolService, StaffService staffService) {
		this.groupService = groupService;
		this.studentService = studentService;
		this.schoolService = schoolService;
		this.staffService = staffService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int studentID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "studentID");
			studentID = Integer.parseInt(requestParameters.get("studentID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		} 
		
		JSONObject responseObject = new JSONObject();
		
		JSONArray joinedGroups = new JSONArray();
		for(Group group : studentService.getJoinedGroups(studentID)){
			
			int groupID = group.getGroupID();
			
			JSONObject groupDetails = new JSONObject();
			groupDetails.put("groupID", groupID);
			groupDetails.put("groupName", group.getGroupName());
			groupDetails.put("groupDescription", group.getGroupDescription());
			groupDetails.put("groupMemberCount", groupService.getMemberCount(group.getGroupID()));
			groupDetails.put("groupTypeName", group.getGroupType().toString());
			groupDetails.put("groupJoinDateUnix", Utilities.toUnixSeconds(studentService.getStudentGroupJoinDate(groupID, studentID)));

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
