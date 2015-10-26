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

import pie.Group;
import pie.GroupHomework;
import pie.Homework;
import pie.Staff;
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
public class SavePublishedHomeworkAsDraftServlet extends HttpServlet{


	private static final long serialVersionUID = -8160625180930523258L;

	GroupHomeworkService groupHomeworkService;
	UserHomeworkService userHomeworkService;
	StaffService staffService;
	GroupService groupService;
	HomeworkService homeworkService;
	StudentGroupService studentGroupService;
	StaffGroupService staffGroupService;
	
	
	@Inject
	public SavePublishedHomeworkAsDraftServlet(GroupHomeworkService groupHomeworkService, UserHomeworkService userHomeworkService, StaffService staffService,
			GroupService groupService, HomeworkService homeworkService, StudentGroupService studentGroupService, StaffGroupService staffGroupService) {
		this.groupHomeworkService = groupHomeworkService;
		this.userHomeworkService = userHomeworkService;
		this.staffService = staffService;
		this.groupService = groupService;
		this.homeworkService = homeworkService;
		this.studentGroupService = studentGroupService;
		this.staffGroupService = staffGroupService;
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
		boolean isDraft = true;
		boolean isGraded = false;
		boolean isDeleted = false;
		int groupHomeworkID = -1;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "homeworkID", "staffID", 
					"markingEffort", "targetMarkingCompletionDate", "dueDate", "isGraded");

			publisher = staffService.getStaff(Integer.parseInt(requestParameters.get("staffID")));
			group = groupService.getGroup(Integer.parseInt(requestParameters.get("groupID")));
			homework = homeworkService.getHomework(Integer.parseInt(requestParameters.get("homeworkID")));
			markingEffort = Integer.parseInt(requestParameters.get("markingEffort"));
			actualMarkingCompletionDate = Utilities.parseClientDate("1000-01-01");
			targetMarkingCompletionDate = Utilities.parseClientDate(requestParameters.get("targetMarkingCompletionDate"));
			dueDate = Utilities.parseClientDate(requestParameters.get("dueDate"));
			isGraded = Integer.parseInt(requestParameters.get("isGraded")) == 1 ? true:false;

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		GroupHomework groupHomework = new GroupHomework(groupHomeworkID, group, homework, publisher, markingEffort, actualMarkingCompletionDate, targetMarkingCompletionDate, dueDate, publishDate, isDraft, isGraded, isDeleted);
		
		groupHomeworkID = groupHomeworkService.savePublishedHomeworkAsDraft(groupHomework);
		
		JSONObject responseObject = new JSONObject();
		
		if(groupHomeworkID != -1){
			responseObject.put("result", "Success");
			responseObject.put("message", "Saved Published Homework as Draft");
		}else{
			responseObject.put("result", "Failed to Publish Homework");
			responseObject.put("message", "Failed to Publish Homework to UserHomework");
			
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
