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
import pie.Parent;
import pie.Staff;
import pie.Student;
import pie.services.GroupHomeworkService;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.ParentStudentService;
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
	ParentStudentService parentStudentService;

	@Inject
	public SendDraftPublishedHomeworkServlet(GroupHomeworkService groupHomeworkService,
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
		int groupHomeworkID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "homeworkID",
					"staffID",
					"markingEffort", "targetMarkingCompletionDate", "dueDate",
					"isGraded", "groupHomeworkID");
			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));
			publisher = staffService.getStaff(Integer.parseInt(requestParameters.get("staffID")));
			group = groupService.getGroup(Integer.parseInt(requestParameters.get("groupID")));
			homework = homeworkService.getHomework(Integer.parseInt(requestParameters.get("homeworkID")));
			markingEffort = Integer.parseInt(requestParameters.get("markingEffort"));
			actualMarkingCompletionDate = Utilities.parseClientDate("1000-01-01");
			targetMarkingCompletionDate = Utilities.parseClientDate(requestParameters.get("targetMarkingCompletionDate"));
			dueDate = Utilities.parseClientDate(requestParameters.get("dueDate"));
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
				if (userHomeworkService.createUserHomework(student.getUserID(), homework.getHomeworkID()) == -1) {
					responseObject.put("result", "Failed to Send Homework to student");
					responseObject.put("message", "Failed to Publish Homework to UserHomework");
					break;
				}
				Parent[] parents = parentStudentService.getParents(student.getUserID());
				
				for(Parent parent : parents){
					if(userHomeworkService.createUserHomework(parent.getUserID(), homework.getHomeworkID()) == -1){
						responseObject.put("result", "Failed to Send Homework to parent");
						responseObject.put("message", "Failed to Publish Homework to UserHomework");
						break;
					}
				}
				
			}

			for (Staff staff : staffMembers) {
				if (userHomeworkService.createUserHomework(staff.getUserID(), homework.getHomeworkID()) == -1) {
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
