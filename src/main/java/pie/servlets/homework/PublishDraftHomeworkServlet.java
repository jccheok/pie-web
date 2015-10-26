package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Homework;
import pie.constants.PublishHomeworkResult;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class PublishDraftHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 1682436975726322347L;

	/*
	 * author: cheok jia chin test on: 21/10/2015
	 */
	HomeworkService homeworkService;

	@Inject
	public PublishDraftHomeworkServlet(HomeworkService homeworkService) {
		this.homeworkService = homeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Homework homework = null;

		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "homeworkID", "staffID",
					"homeworkTitle", "homeworkSubject", "homeworkDescription", "homeworkMinutesReqStudent",
					"homeworkLevel");

			homework = homeworkService.getHomework(Integer.parseInt(requestParams.get("homeworkID")));
			homework.setHomeworkSubject(requestParams.get("homeworkSubject"));
			homework.setHomeworkDescription(requestParams.get("homeworkDescription"));
			homework.setHomeworkTitle(requestParams.get("homeworkTitle"));
			homework.sethomeworkMinutesReqStudent(Integer.parseInt(requestParams.get("homeworkMinutesReqStudent")));
			homework.setHomeworkLevel(requestParams.get("homeworkLevel"));

			

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		PublishHomeworkResult result = homeworkService.publishDraftHomework(homework);

		JSONObject responseObject = new JSONObject();

		responseObject.put("result", result.toString());
		responseObject.put("message", result.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
