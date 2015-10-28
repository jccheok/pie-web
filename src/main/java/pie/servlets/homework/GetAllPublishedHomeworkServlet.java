package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;

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
public class GetAllPublishedHomeworkServlet extends HttpServlet {
	/*
	 * author: cheok jia chin modified: 22/10/2015
	 */
	private static final long serialVersionUID = 5438895086826432496L;

	private HomeworkService homeworkService;

	@Inject
	public GetAllPublishedHomeworkServlet(HomeworkService homeworkService, GroupService groupService,
			StaffService staffService) {
		this.homeworkService = homeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject responseObj = new JSONObject();

		Homework[] publishedHomework = homeworkService.getAllPublishedHomework();

		JSONArray listHomework = new JSONArray();

		for (Homework homework : publishedHomework) {
			JSONObject jsonHomework = new JSONObject();
			String description = Utilities.parseHtml(homework.getHomeworkDescription());
			jsonHomework.put("staffID", homework.getHomeworkAuthor().getUserID());
			jsonHomework.put("homeworkID", homework.getHomeworkID());
			jsonHomework.put("title", homework.getHomeworkTitle());
			jsonHomework.put("subject", homework.getHomeworkSubject());
			jsonHomework.put("description", description);
			listHomework.put(jsonHomework);
		}

		responseObj.put("listPublishedHomework", listHomework);

		PrintWriter out = response.getWriter();
		out.write(responseObj.toString());
	}
}
