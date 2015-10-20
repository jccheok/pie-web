package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Homework;
import pie.constants.PublishHomeworkResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class PublishDraftHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 1682436975726322347L;

	HomeworkService homeworkService;
	GroupService groupService;
	StaffService staffService;

	@Inject
	public PublishDraftHomeworkServlet(HomeworkService homeworkService, GroupService groupService,
			StaffService staffService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PublishHomeworkResult result = null;

		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "homeworkID", "authorID",
					"homeworkTitle", "homeworkSubject", "homeworkDescription", "homeworkMinutesReqStudent",
					"homeworkLevel");

			Homework homework = homeworkService.getHomework(Integer.parseInt(requestParams.get("homeworkID")));
			homework.setHomeworkSubject(requestParams.get("homeworkSubject"));
			homework.setHomeworkDescription(requestParams.get("homeworkDescription"));
			homework.setHomeworkTitle(requestParams.get("homeworkTitle"));
			homework.sethomeworkMinutesReqStudent(Integer.parseInt(requestParams.get("homeworkMinutesReqStudent")));
			homework.setHomeworkLevel(requestParams.get("homeworkLevel"));

			result = homeworkService.publishDraftHomework(homework);

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject responseObject = new JSONObject();

		responseObject.put("result", result.toString());
		responseObject.put("message", result.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
