package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.GroupHomework;
import pie.services.GroupHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetPublishedHomeworkDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 2290803580035753863L;
	GroupHomeworkService groupHomeworkService;

	@Inject
	public GetPublishedHomeworkDetailsServlet(GroupHomeworkService groupHomeworkService) {
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupHomeworkID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupHomeworkID");
			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		GroupHomework groupHomework = groupHomeworkService.getGroupHomework(groupHomeworkID);

		JSONObject responseObject = new JSONObject();
		if (groupHomework != null) {
		
			JSONObject homeworkObject = new JSONObject();
			
			homeworkObject.put("groupHomeworkID", groupHomework.getGroupHomeworkID());
			homeworkObject.put("homeworkID", groupHomework.getHomework().getHomeworkID());
			homeworkObject.put("homeworkTitle", groupHomework.getHomework().getHomeworkTitle());
			homeworkObject.put("homeworkDescription", groupHomework.getHomework().getHomeworkDescription());
			homeworkObject.put("subject", groupHomework.getHomework().getHomeworkSubject());
			homeworkObject.put("minutesRequired", groupHomework.getHomework().gethomeworkMinutesReqStudent());
			homeworkObject.put("dueDate", groupHomework.getDueDate());
			homeworkObject.put("publisherID", groupHomework.getPublisher().getUserID());
			homeworkObject.put("publisherName", groupHomework.getPublisher().getUserFullName());
			homeworkObject.put("publishedDate", Utilities.toUnixSeconds(groupHomework.getPublishDate()));
			homeworkObject.put("markingEffort", groupHomework.getMarkingEffort());
			homeworkObject.put("actualMarkingCompletionDate", groupHomework.getActualMarkingCompletionDate());
			homeworkObject.put("targetMarkingCompletionDate", groupHomework.getTargetMarkingCompletionDate());
			homeworkObject.put("isGraded", groupHomework.isGraded());
			homeworkObject.put("group", groupHomework.getGroup().getGroupName());
			
			responseObject.put("publishedHomeworkDetails", homeworkObject);
			
		} else {
			responseObject.put("result", "Homework does not exist");
			responseObject.put("message", "There is no such homework.");
		}
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
