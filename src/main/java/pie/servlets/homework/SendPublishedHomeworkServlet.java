package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Group;
import pie.GroupHomework;
import pie.Homework;
import pie.Staff;
import pie.services.GroupHomeworkService;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.ParentStudentService;
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.services.StudentGroupService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

@Singleton
public class SendPublishedHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 7233493980305643997L;

	GroupHomeworkService groupHomeworkService;
	UserHomeworkService userHomeworkService;
	StaffService staffService;
	GroupService groupService;
	HomeworkService homeworkService;
	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;
	ParentStudentService parentStudentService;

	@Inject
	public SendPublishedHomeworkServlet(GroupHomeworkService groupHomeworkService,
			UserHomeworkService userHomeworkService, StaffService staffService,
			GroupService groupService, HomeworkService homeworkService, StudentGroupService studentGroupService,
			StaffGroupService staffGroupService, ParentStudentService parentStudentService) {
		this.groupHomeworkService = groupHomeworkService;
		this.userHomeworkService = userHomeworkService;
		this.staffService = staffService;
		this.groupService = groupService;
		this.homeworkService = homeworkService;
		this.studentGroupService = studentGroupService;
		this.staffGroupService = staffGroupService;
		this.parentStudentService = parentStudentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
		int groupHomeworkID = -1;
		String instructions = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "homeworkID",
					"staffID",
					"markingEffort", "targetMarkingCompletionDate", "dueDate", "isGraded", "instructions");

			publisher = staffService.getStaff(Integer.parseInt(requestParameters.get("staffID")));
			group = groupService.getGroup(Integer.parseInt(requestParameters.get("groupID")));
			homework = homeworkService.getHomework(Integer.parseInt(requestParameters.get("homeworkID")));
			markingEffort = Integer.parseInt(requestParameters.get("markingEffort"));
			actualMarkingCompletionDate = Utilities.parseClientDate("1000-01-01");
			targetMarkingCompletionDate = Utilities.parseClientDate(requestParameters.get("targetMarkingCompletionDate"));
			dueDate = Utilities.parseClientDate(requestParameters.get("dueDate"));
			isGraded = Integer.parseInt(requestParameters.get("isGraded")) == 1 ? true : false;
			instructions = requestParameters.get("instructions");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		GroupHomework groupHomework = new GroupHomework(groupHomeworkID, group, homework, publisher, markingEffort,
				actualMarkingCompletionDate, targetMarkingCompletionDate, dueDate, publishDate, isDraft, isGraded,
				isDeleted, instructions);

		groupHomeworkID = groupHomeworkService.sendPublishedHomework(groupHomework);

		JSONObject responseObject = new JSONObject();

		if (groupHomeworkID != -1) {
			boolean sendResult = userHomeworkService.createUserHomework(homework.getHomeworkID(), group.getGroupID());
			
			if(sendResult){
				responseObject.put("result", "SUCCESS");
				responseObject.put("message", "Sent Published Homework to all users");
			}else{
				responseObject.put("result", "FAILED");
				responseObject.put("message", "Failed to publish homework to users");
			}
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Failed to Publish Homework to UserHomework");

		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
