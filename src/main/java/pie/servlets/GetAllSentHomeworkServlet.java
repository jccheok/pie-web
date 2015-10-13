package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Homework;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class GetAllSentHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 3561732142697312970L;

	HomeworkService homeworkService;
	GroupService groupService;

	@Inject
	public GetAllSentHomeworkServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();

		int staffID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID");
			staffID = Integer.parseInt(requestParameters.get("staffID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Homework[] listSentHomework = homeworkService.getSentHomework(staffID);

		JSONArray homeworkList = new JSONArray();

		for (Homework homework : listSentHomework) {
			JSONObject homeworkObject = new JSONObject();
			homeworkObject.put("homeworkID", homework.getHomeworkID());
			homeworkObject.put("homeworkDateCreated", homework.getHomeworkDateCreated());
			homeworkObject.put("homeworkDescription", homework.getHomeworkDescription());
			homeworkObject.put("homeworkDueDateUnix", Utilities.toUnixSeconds(homework.getHomeworkDueDate()));
			homeworkObject.put("homeworkTitle", homework.getHomeworkTitle());
			homeworkObject.put("homeworkSubject", homework.getHomeworkSubject());
			homeworkList.put(homeworkObject);
		}

		responseObject.put("sentHomework", homeworkList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
