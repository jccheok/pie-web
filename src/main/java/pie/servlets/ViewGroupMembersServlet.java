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

import pie.Staff;
import pie.StaffRole;
import pie.Student;
import pie.services.GroupService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class ViewGroupMembersServlet {

	GroupService groupService;
	
	@Inject
	public ViewGroupMembersServlet(GroupService groupService) {
		this.groupService = groupService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		int groupID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		} 
		
		Student[] studentMembers = groupService.getStudentMembers(groupID);
		Staff[] staffMembers = groupService.getStaffMembers(groupID);

		JSONObject responseObject = new JSONObject();
		
		JSONArray studentList = new JSONArray();
		
		for(Student student : studentMembers){
			HashMap<String, String> students = new HashMap<String, String>();
			students.put("studentFullName", student.getUserFullName());
			students.put("studentMobile", student.getUserMobile());
			studentList.put(students);
		}
				
		JSONArray staffList = new JSONArray();
		
		StaffService staffService = new StaffService();
		
		for(Staff staff : staffMembers){
			StaffRole staffRole = staffService.getStaffRole(staff.getUserID(), groupID);
			HashMap<String, String> staffs = new HashMap<String, String>();
			staffs.put("staffFullName", staff.getUserFullName());
			staffs.put("staffMobile", staff.getUserMobile());
			staffs.put("staffRole", staffRole.getStaffRoleName());
			staffList.put(staffs);
		}
		
		responseObject.put("studentMembers", studentList);
		responseObject.put("staffMembers", staffList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
