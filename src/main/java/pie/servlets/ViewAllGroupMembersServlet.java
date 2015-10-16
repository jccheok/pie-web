
package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Group;
import pie.Staff;
import pie.Student;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.services.StudentGroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewAllGroupMembersServlet extends HttpServlet{


	private static final long serialVersionUID = 8502515641450520829L;
	
	GroupService groupService;
	StaffService staffService;
	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;
	
	@Inject
	public ViewAllGroupMembersServlet(GroupService groupService, StaffService staffService, StudentGroupService studentGroupService, StaffGroupService staffGroupService) {
		this.groupService = groupService;
		this.staffService = staffService;
		this.studentGroupService = studentGroupService;
		this.staffGroupService = staffGroupService;
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
		
		Group[] groups = staffService.getJoinedGroups(staffID);
		JSONObject responseObject = new JSONObject();
		JSONArray groupList = new JSONArray();
		try{
			
			for(Group group : groups){
				JSONObject groupMembers = new JSONObject();
				
				Student[] studentMembers = studentGroupService.getStudentMembers(group.getGroupID());
				Staff[] staffMembers = staffGroupService.getStaffMembers(group.getGroupID());
				
				JSONArray studentList = new JSONArray();

				for(Student student : studentMembers){
					HashMap<String, String> students = new HashMap<String, String>();
					students.put("studentFullName", student.getUserFullName());
					students.put("studentEmail", student.getUserEmail());
					studentList.put(students);
				}
				
				JSONArray staffList = new JSONArray();
								
				for(Staff staff : staffMembers){
					HashMap<String, String> staffs = new HashMap<String, String>();
					staffs.put("staffFullName", staff.getUserFullName());
					staffs.put("staffEmail", staff.getUserEmail());
					staffList.put(staffs);
				}
				
				groupMembers.put("studentMembers", studentList);
				groupMembers.put("staffMembers", staffList);
				groupList.put(groupMembers);
			}
			
			responseObject.put("groupList", groupList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
