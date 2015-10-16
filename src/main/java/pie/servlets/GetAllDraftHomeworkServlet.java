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
		Homework[] listHomework = {};
		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "authorID");

			listHomework = homeworkService.getAllDraftHomework(Integer.parseInt(requestParams.get("authorID")));

		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject();
		JSONArray jsonArrayHomework = new JSONArray();
		for (Homework homework : listHomework) {
			JSONObject jsonHomework = new JSONObject();
			jsonHomework.put("title", homework.getHomeworkTitle());
			jsonHomework.put("subject", homework.getHomeworkSubject());
			jsonHomework.put("description", homework.getHomeworkDescription());
			jsonArrayHomework.put(jsonHomework);
		}

		responseObject.put("listHomeworks", jsonArrayHomework);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
}
