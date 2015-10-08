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
import pie.Student;
import pie.services.ParentService;
import pie.services.StudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewParentChildrenServlet extends HttpServlet {

	private static final long serialVersionUID = 11940297912398677L;
	
	ParentService parentService;
	StudentService studentService;
	
	@Inject
	public ViewParentChildrenServlet(ParentService parentService, StudentService studentService) {
		this.parentService = parentService;
		this.studentService = studentService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int parentID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "parentID");
			parentID = Integer.parseInt(requestParameters.get("parentID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		
		JSONArray childrenList = new JSONArray();
		for (Student student : parentService.getChildren(parentID)) {
			
			int studentID = student.getUserID();
			
			JSONObject studentDetails = new JSONObject();
			studentDetails.put("studentID", studentID);
			studentDetails.put("studentFirstName", student.getUserFirstName());
			studentDetails.put("studentLastName", student.getUserLastName());
			studentDetails.put("studentSchoolName", student.getSchool().getSchoolName());
			studentDetails.put("studentSchoolID", student.getSchool().getSchoolID());
			
			JSONArray studentGroupsList = new JSONArray();
			for (Group joinedGroup : studentService.getJoinedGroups(studentID)) {
				
				int joinedGroupID = joinedGroup.getGroupID();
				
				JSONObject groupDetails = new JSONObject();
				groupDetails.put("groupID", joinedGroupID);
				groupDetails.put("groupName", joinedGroup.getGroupName());
				groupDetails.put("groupTypeName", joinedGroup.getGroupType().toString());
				groupDetails.put("studentGroupIndexNumber", studentService.getStudentGroupIndexNumber(joinedGroupID, studentID));
				groupDetails.put("studentGroupJoinDateUnix", Utilities.toUnixSeconds(studentService.getStudentGroupJoinDate(joinedGroupID, studentID)));
				studentGroupsList.put(groupDetails);
			}
			studentDetails.put("studentJoinedGroups", studentGroupsList);
			childrenList.put(studentDetails);
		}
		responseObject.put("parentChildren", childrenList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
