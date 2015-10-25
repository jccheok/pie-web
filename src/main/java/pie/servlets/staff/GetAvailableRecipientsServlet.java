package pie.servlets.staff;

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
import pie.Student;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.services.StudentGroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAvailableRecipientsServlet extends HttpServlet {

	private static final long serialVersionUID = 8502515641450520829L;

	GroupService groupService;
	StaffService staffService;
	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;

	@Inject
	public GetAvailableRecipientsServlet(GroupService groupService, StaffService staffService,
			StudentGroupService studentGroupService, StaffGroupService staffGroupService) {
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

		Group[] groups = staffGroupService.getJoinedGroups(staffID);
		JSONObject responseObject = new JSONObject();

		try {
			JSONArray groupList = new JSONArray();
			JSONArray memberList = new JSONArray();

			for (Group group : groups) {
				JSONObject groupDetails = new JSONObject();

				groupDetails.put("groupID", group.getGroupID());
				groupDetails.put("groupName", group.getGroupName());

				groupList.put(groupDetails);

				Student[] studentMembers = studentGroupService.getStudentMembers(group.getGroupID());
				Staff[] staffMembers = staffGroupService.getStaffMembers(group.getGroupID());

				for (Student student : studentMembers) {
					JSONObject memberDetails = new JSONObject();
					memberDetails.put("studentID", student.getUserID());
					memberDetails.put("fullName", student.getUserFullName());
					memberDetails.put("studentEmail", student.getUserEmail());

					memberList.put(memberDetails);
				}

				for (Staff staff : staffMembers) {
					JSONObject memberDetails = new JSONObject();
					memberDetails.put("staffID", staff.getUserID());
					memberDetails.put("fullName", staff.getUserFullName());
					memberDetails.put("staffEmail", staff.getUserEmail());
					
					memberList.put(memberDetails);
				}

			}

			responseObject.put("groupList", groupList);
			responseObject.put("memberList", memberList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
