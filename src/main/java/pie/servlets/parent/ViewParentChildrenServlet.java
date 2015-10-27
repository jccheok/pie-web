package pie.servlets.parent;

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
import pie.GroupHomework;
import pie.Parent;
import pie.Student;
import pie.UserHomework;
import pie.services.GroupHomeworkService;
import pie.services.ParentStudentService;
import pie.services.StudentGroupService;
import pie.services.StudentService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewParentChildrenServlet extends HttpServlet {

	private static final long serialVersionUID = 11940297912398677L;
	
	ParentStudentService parentStudentService;
	StudentService studentService;
	StudentGroupService studentGroupService;
	UserHomeworkService userHomeworkService;
	GroupHomeworkService groupHomeworkService;
	
	@Inject
	public ViewParentChildrenServlet(ParentStudentService parentStudentService, StudentService studentService, 
			StudentGroupService studentGroupService, UserHomeworkService userHomeworkService, GroupHomeworkService groupHomeworkService) {
		this.parentStudentService = parentStudentService;
		this.studentService = studentService;
		this.studentGroupService = studentGroupService;
		this.userHomeworkService = userHomeworkService;
		this.groupHomeworkService = groupHomeworkService;
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
		for (Student student : parentStudentService.getChildren(parentID)) {
			
			int studentID = student.getUserID();
			Parent mainParent = parentStudentService.getMainParent(studentID);
			
			JSONObject studentDetails = new JSONObject();
			studentDetails.put("studentID", studentID);
			studentDetails.put("studentFirstName", student.getUserFirstName());
			studentDetails.put("studentLastName", student.getUserLastName());
			studentDetails.put("studentSchoolName", student.getSchool().getSchoolName());
			studentDetails.put("studentSchoolID", student.getSchool().getSchoolID());
			studentDetails.put("mainParentID", mainParent.getUserID());
			studentDetails.put("mainParentFirstName", mainParent.getUserFirstName());
			
			JSONArray studentGroupsList = new JSONArray();
			for (Group joinedGroup : studentService.getJoinedGroups(studentID)) {
				
				int joinedGroupID = joinedGroup.getGroupID();
				
				JSONObject groupDetails = new JSONObject();
				groupDetails.put("groupID", joinedGroupID);
				groupDetails.put("groupName", joinedGroup.getGroupName());
				groupDetails.put("groupTypeName", joinedGroup.getGroupType().toString());
				groupDetails.put("studentGroupIndexNumber", studentService.getStudentGroupIndexNumber(joinedGroupID, studentID));
				groupDetails.put("studentGroupJoinDateUnix", Utilities.toUnixSeconds(studentGroupService.getStudentGroupJoinDate(joinedGroupID, studentID)));
				studentGroupsList.put(groupDetails);
			}
			
			JSONArray studentHomeworkList = new JSONArray();
			for(UserHomework userHomework : userHomeworkService.getAllUserHomework(studentID)){
				
				JSONObject homeworkDetails = new JSONObject();
				
				int userHomeworkID = userHomework.getUserHomeworkID();
				GroupHomework groupHomework = groupHomeworkService.getGroupHomework(userHomework.getGroupHomeworkID());
				
				homeworkDetails.put("homeworkTitle", userHomework.getHomework().getHomeworkTitle());
				homeworkDetails.put("dueDate", Utilities.parseServletDateFormat(groupHomework.getDueDate()));
				homeworkDetails.put("grade", userHomework.getGrade());
				homeworkDetails.put("userHomeworkID", userHomeworkID);
				homeworkDetails.put("homeworkID", userHomework.getHomework().getHomeworkID());
				
				if (!userHomework.isSubmitted()) {
					homeworkDetails.put("status", "Not Submitted");
				} else if (!userHomework.isMarked()) {
					homeworkDetails.put("status", "Submitted");
				} else if (!userHomework.getGrade().equals("-")) {
					homeworkDetails.put("status", "Marked");
				} else {
					homeworkDetails.put("status", "Graded");
				}
				
				studentHomeworkList.put(homeworkDetails);
				
			}
			studentDetails.put("studentJoinedGroups", studentGroupsList);
			studentDetails.put("studentHomework", studentHomeworkList);
			childrenList.put(studentDetails);
		}
		responseObject.put("parentChildren", childrenList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
