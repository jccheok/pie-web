package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Group;
import pie.GroupHomework;
import pie.Homework;
import pie.Staff;
import pie.Student;
import pie.services.GroupHomeworkService;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.services.StudentGroupService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SendDraftPublishedHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -1957825813083294498L;
	
	GroupHomeworkService groupHomeworkService;
	UserHomeworkService userHomeworkService;
	StaffService staffService;
	GroupService groupService;
	HomeworkService homeworkService;
	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;

	@Inject
	public SendDraftPublishedHomeworkServlet(GroupHomeworkService groupHomeworkService,
			UserHomeworkService userHomeworkService, StaffService staffService,
			GroupService groupService, HomeworkService homeworkService, StudentGroupService studentGroupService,
			StaffGroupService staffGroupService) {
		this.groupHomeworkService = groupHomeworkService;
		this.userHomeworkService = userHomeworkService;
		this.staffService = staffService;
		this.groupService = groupService;
		this.homeworkService = homeworkService;
		this.studentGroupService = studentGroupService;
		this.staffGroupService = staffGroupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Group group = null;
		Homework homework = null;
		Staff publisher = null;
		int markingEffort = 0;
		Date actualMarkingCompletionDate = null;
		Date targetMarkingCompletionDate = null;
		Date dueDate = null;
		Date publishDate = null;
		boolean isDraft = false;
		boolean isGraded = false;
		boolean isDeleted = false;
		int groupHomeworkID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "homeworkID",
					"staffID",
					"markingEffort", "actualMarkingCompletionDate", "targetMarkingCompletionDate", "dueDate",
					"isGraded", "groupHomeworkID");
			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));
			publisher = staffService.getStaff(Integer.parseInt(requestParameters.get("staffID")));
			group = groupService.getGroup(Integer.parseInt(requestParameters.get("groupID")));
			homework = homeworkService.getHomework(Integer.parseInt(requestParameters.get("homeworkID")));
			markingEffort = Integer.parseInt(requestParameters.get("markingEffort"));
			actualMarkingCompletionDate = dateFormat.parse(requestParameters.get("actualMarkingCompletionDate"));
			targetMarkingCompletionDate = dateFormat.parse(requestParameters.get("targetMarkingCompletionDate"));
			dueDate = dateFormat.parse(requestParameters.get("dueDate"));
			isGraded = Integer.parseInt(requestParameters.get("isGraded")) == 1 ? true : false;

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		GroupHomework groupHomework = new GroupHomework(groupHomeworkID, group, homework, publisher, markingEffort,
				actualMarkingCompletionDate, targetMarkingCompletionDate, dueDate, publishDate, isDraft, isGraded,
				isDeleted);

		JSONObject responseObject = new JSONObject();

		if(!groupHomeworkService.updateDraftPublishedHomework(groupHomework)){
			
			responseObject.put("result", "Failed to Update Published Homework");
			responseObject.put("message", "Failed to Update PublishedHomework");
			
		}else if(!groupHomeworkService.setDraftPublishedHomework(groupHomeworkID)){
			
			responseObject.put("result", "Failed to set to Not Draft");
			responseObject.put("message", "Failed to set Published Homework to not Draft");
			
		}else{
			Student[] studentMembers = studentGroupService.getStudentMembers(group.getGroupID());
			Staff[] staffMembers = staffGroupService.getStaffMembers(group.getGroupID());

			for (Student student : studentMembers) {
				if (!userHomeworkService.sendHomework(student.getUserID(), homework.getHomeworkID())) {
					responseObject.put("result", "Failed to Send Homework to student");
					responseObject.put("message", "Failed to Publish Homework to UserHomework");
					break;
				}
			}

			for (Staff staff : staffMembers) {
				if (!userHomeworkService.sendHomework(staff.getUserID(), homework.getHomeworkID())) {
					responseObject.put("result", "Failed to Send Homework to staff");
					responseObject.put("message", "Failed to Publish Homework to UserHomework");
					break;
				}
			}
			responseObject.put("result", "Success");
			responseObject.put("message", "Sent Published Homework to all users");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
