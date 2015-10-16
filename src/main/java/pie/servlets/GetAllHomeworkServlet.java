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

import pie.Group;
import pie.Homework;
import pie.services.HomeworkService;
import pie.services.StaffService;
import pie.utilities.Utilities;

@Singleton
public class GetAllHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 7604701513017149675L;

	HomeworkService homeworkService;
	StaffService staffService;

	@Inject
	public GetAllHomeworkServlet(HomeworkService homeworkService, StaffService staffService) {
		this.homeworkService = homeworkService;
		this.staffService = staffService;
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

		Group[] staffGroup = staffService.getJoinedGroups(staffID);
		JSONArray groupHomeworks = new JSONArray();

		for (Group group : staffGroup) {

			Homework[] allHomeworks = homeworkService.getAllHomework(group.getGroupID());

			for (Homework homework : allHomeworks) {
				JSONObject homeworkObject = new JSONObject();
				homeworkObject.put("homeworkTitle", homework.getHomeworkTitle());
				homeworkObject.put("homeworkSubject", homework.getHomeworkSubject());
//				homeworkObject.put("homeworkDueDate", Utilities.toUnixSeconds(homework.getHomeworkDueDate()));
				homeworkObject.put("homeworkDescription", homework.getHomeworkDescription());
				homeworkObject.put("homeworkAuthor", homework.getHomeworkAuthor().getUserFullName());
				groupHomeworks.put(homeworkObject);
			}
		}

		responseObject.put("allHomework", groupHomeworks);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
