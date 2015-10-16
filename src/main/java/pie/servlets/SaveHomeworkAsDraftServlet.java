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
import pie.constants.SaveHomeworkAsDraftResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.StaffService;
import pie.utilities.Utilities;

public class SaveHomeworkAsDraftServlet extends HttpServlet {

	private static final long serialVersionUID = -4157557648698715877L;
	HomeworkService homeworkService;
	GroupService groupService;
	StaffService staffService;

	@Inject
	public SaveHomeworkAsDraftServlet(HomeworkService homeworkService, GroupService groupService,
			StaffService staffService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int result = 0;
		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "authorID", "homeworkTitle",
					"homeworkSubject", "homeworkDescription", "homeworkMinutesReqStudent", "homeworkLevel");

			Homework homework = new Homework(0, staffService.getStaff(Integer.parseInt(requestParams.get("authorID"))),
					requestParams.get("homeworkTitle"), requestParams.get("homeworkSubject"),
					requestParams.get("homeworkDescription"),
					Integer.parseInt(requestParams.get("homeworkMinutesReqStudent")), null, false, false, null,
					requestParams.get("homeworkLevel"));

			result = homeworkService.saveHomeworkAsDraft(homework);

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject responseObject = new JSONObject();

		if (result == -1) {

			responseObject.put("result", SaveHomeworkAsDraftResult.FAILED_TO_SAVE_HOMEWORK_AS_DRAFT.toString());
			responseObject.put("message",
					SaveHomeworkAsDraftResult.FAILED_TO_SAVE_HOMEWORK_AS_DRAFT.getDefaultMessage());
		} else {
			responseObject.put("result", SaveHomeworkAsDraftResult.SUCCESS.toString());
			responseObject.put("message", SaveHomeworkAsDraftResult.SUCCESS.getDefaultMessage());
		}
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
