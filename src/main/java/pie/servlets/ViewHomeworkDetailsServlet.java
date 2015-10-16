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
import com.google.inject.Singleton;

import pie.Homework;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class ViewHomeworkDetailsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 7727272635613038515L;
	
	HomeworkService homeworkService;

	@Inject
	public ViewHomeworkDetailsServlet(HomeworkService homeworkService) {
		this.homeworkService = homeworkService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();

		int homeworkID = 0;

		try {
			Map<String, String> requestParameters = Utilities.getParameters(request, "homeworkID");
			homeworkID = Integer.parseInt(requestParameters.get("homeworkID"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Homework homework = homeworkService.getHomework(homeworkID);

		responseObject.put("homeworkSubject", homework.getHomeworkSubject());
		responseObject.put("homeworkTitle", homework.getHomeworkTitle());
		responseObject.put("homeworkMinutesRequired", homework.gethomeworkMinutesReqStudent());
		responseObject.put("homeworkDateCreated", Utilities.toUnixSeconds(homework.getHomeworkDateCreated()));
		responseObject.put("homeworkAuthor", homework.getHomeworkAuthor().getUserFullName());
		responseObject.put("homeworkDescription", homework.getHomeworkDescription());
//		responseObject.put("homeworkDueDate", Utilities.toUnixSeconds(homework.getHomeworkDueDate()));

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
