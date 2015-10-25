package pie.servlets.groups;

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

import pie.Parent;
import pie.Staff;
import pie.StaffRole;
import pie.Student;
import pie.services.ParentStudentService;
import pie.services.StaffGroupService;
import pie.services.StudentGroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewGroupMembersServlet extends HttpServlet{

	private static final long serialVersionUID = 2879025505322510247L;

	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;
	ParentStudentService parentStudentService;

	@Inject
	public ViewGroupMembersServlet(StudentGroupService studentGroupService, StaffGroupService staffGroupService, ParentStudentService parentStudentService) {
		this.studentGroupService = studentGroupService;
		this.staffGroupService = staffGroupService;
		this.parentStudentService = parentStudentService;
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
		
		Student[] studentMembers = studentGroupService.getStudentMembers(groupID);
		Staff[] staffMembers = staffGroupService.getStaffMembers(groupID);

		JSONObject responseObject = new JSONObject();
		
		JSONArray studentList = new JSONArray();
		
		for(Student student : studentMembers){
			
			Parent mainParent = parentStudentService.getMainParent(student.getUserID());
			HashMap<String, String> students = new HashMap<String, String>();
			students.put("studentFullName", student.getUserFullName());
			students.put("studentMobile", student.getUserMobile());
			students.put("studentID", Integer.toString(student.getUserID()));
			students.put("mainParent" , mainParent.getUserFullName());
			students.put("studentMobile", student.getUserMobile());
			studentList.put(students);
		}
				
		JSONArray staffList = new JSONArray();
		
		for(Staff staff : staffMembers){
			StaffRole staffRole = staffGroupService.getStaffRole(staff.getUserID(), groupID);
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
