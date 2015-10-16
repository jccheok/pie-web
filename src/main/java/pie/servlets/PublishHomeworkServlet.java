package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;

import pie.Homework;
import pie.Staff;
import pie.constants.PublishHomeworkResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.StaffService;
import pie.utilities.Utilities;

public class PublishHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = -6931499866512426095L;
	HomeworkService homeworkService;
	GroupService groupService;
	StaffService staffService;

	@Inject
	public PublishHomeworkServlet(HomeworkService homeworkService, GroupService groupService,
			StaffService staffService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Staff homeworkAuthor = null;
		String homeworkTitle = null;
		String homeworkSubject = null;
		String homeworkDescription = null;
		int homeworkMinutesReqStudent = 0;
		String homeworkLevel = null;

		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "authorID", "homeworkTitle",
					"homeworkSubject", "homeworkDescription", "homeworkMinutesReqStudent", "homeworkLevel");

			homeworkAuthor = staffService.getStaff(Integer.parseInt(requestParams.get("authorID")));
			homeworkTitle = requestParams.get("homeworkTitle");
			homeworkSubject = requestParams.get("homeworkSubject");
			homeworkDescription = requestParams.get("homeworkDescription");
			homeworkMinutesReqStudent = Integer.parseInt(requestParams.get("homeworkMinutesReqStudent"));
			homeworkLevel = requestParams.get("homeworkLevel");

		} catch (Exception e) {
			e.printStackTrace();
		}

		Homework homework = new Homework(0, homeworkAuthor, homeworkTitle, homeworkSubject, homeworkDescription,
				homeworkMinutesReqStudent, null, false, false, null, homeworkLevel);

		int homeworkID = homeworkService.publishHomework(homework);

		JSONObject responseObject = new JSONObject();

		PublishHomeworkResult result;
		if (homeworkID == -1) {
			responseObject.put("result", PublishHomeworkResult.FAILED_TO_UPDATE_HOMEWORK.toString());
			responseObject.put("message", PublishHomeworkResult.FAILED_TO_UPDATE_HOMEWORK.getDefaultMessage());
		} else {
			responseObject.put("result", PublishHomeworkResult.SUCCESS.toString());
			responseObject.put("message", PublishHomeworkResult.SUCCESS.getDefaultMessage());
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}