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

import pie.Homework;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllDraftHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = -6427066420333527086L;

	HomeworkService homeworkService;
	GroupService groupService;
	
	@Inject
	public GetAllDraftHomeworkServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();

		int staffID = 0;

		Map<String, String> requestParameters = Utilities.getParameters(request, "staffID");

		staffID = Integer.parseInt(requestParameters.get("staffID"));

		Homework[] draftHomeworkList = homeworkService.getAllDraftHomework(staffID);

		JSONArray homeworkList = new JSONArray();

		for (Homework homework : draftHomeworkList) {
			JSONObject homeworkObject = new JSONObject();
			homeworkObject.put("homeworkID", homework.getHomeworkID());
			homeworkObject.put("homeworkDateCreated", homework.getHomeworkDateCreated());
			homeworkObject.put("homeworkDescription", homework.getHomeworkDescription());
			homeworkObject.put("homeworkDueDateUnix", (long) homework.getHomeworkDueDate().getTime() / 1000);
			homeworkObject.put("homeworkTitle", homework.getHomeworkTitle());
			homeworkObject.put("homeworkSubject", homework.getHomeworkSubject());
			homeworkList.put(homeworkObject);
		}

		responseObject.put("draftHomework", homeworkList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
