package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Homework;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetHomeworkDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = -3922470004464322114L;

	private HomeworkService homeworkService;

	@Inject
	public GetHomeworkDetailsServlet(HomeworkService homeworkService) {
		this.homeworkService = homeworkService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int homeworkID = -1;

		try {

			Map<String, String> requestParams = Utilities.getParameters(request, "homeworkID");
			homeworkID = Integer.parseInt(requestParams.get("homeworkID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Homework homework = homeworkService.getHomework(homeworkID);

		JSONObject responseObject = new JSONObject();

		if (homework != null) {

			responseObject.put("homeworkID", homework.getHomeworkID());
			responseObject.put("dateCreated", Utilities.parseServletDateFormat(homework.getHomeworkDateCreated()));
			responseObject.put("authorName", homework.getHomeworkAuthor().getUserFullName());
			responseObject.put("authorID",homework.getHomeworkAuthor().getUserID());
			responseObject.put("homeworkDescription", homework.getHomeworkDescription());
			responseObject.put("level", homework.getHomeworkLevel());
			responseObject.put("homeworkTitle", homework.getHomeworkTitle());
			responseObject.put("subject", homework.getHomeworkSubject());
			responseObject.put("minutesRequired", homework.gethomeworkMinutesReqStudent());

		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
