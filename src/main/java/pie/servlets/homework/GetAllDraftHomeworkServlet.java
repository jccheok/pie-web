package pie.servlets.homework;

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
import pie.services.StaffService;
import pie.utilities.Utilities;

@Singleton
public class GetAllDraftHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 2348851989151099831L;

	HomeworkService homeworkService;
	GroupService groupService;
	StaffService staffService;

	@Inject
	public GetAllDraftHomeworkServlet(HomeworkService homeworkService, GroupService groupService,
			StaffService staffService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int staffID = 0;

		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "staffID");

			staffID = Integer.parseInt(requestParams.get("staffID"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		Homework[] listHomework = homeworkService.getAllDraftHomework(staffID);

		JSONObject responseObject = new JSONObject();
		JSONArray jsonArrayHomework = new JSONArray();

		if (listHomework != null) {
			for (Homework homework : listHomework) {
				JSONObject jsonHomework = new JSONObject();
				jsonHomework.put("homeworkID", homework.getHomeworkID());
				jsonHomework.put("staffID", homework.getHomeworkAuthor().getUserID());
				jsonHomework.put("title", homework.getHomeworkTitle());
				jsonHomework.put("subject", homework.getHomeworkSubject());
				jsonHomework.put("description", Utilities.parseHtml(homework.getHomeworkDescription()));
				jsonArrayHomework.put(jsonHomework);
			}
		}

		responseObject.put("listHomeworks", jsonArrayHomework);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}
