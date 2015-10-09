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

import pie.constants.PublishHomeworkResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class SendDraftHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 7748623189080892405L;

	HomeworkService homeworkService;
	GroupService groupService;

	@Inject
	public SendDraftHomeworkServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int homeworkID = 0;
		int groupID = 0;

		try {
			Map<String, String> requestParameters = Utilities.getParameters(request, "homeworkID", "groupID");

			homeworkID = Integer.parseInt(requestParameters.get("homeworkID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		//update homework
		PublishHomeworkResult publishHomeworkResult = homeworkService.publishHomework(groupID, homeworkID);
		responseObject.put("result", publishHomeworkResult.toString());
		responseObject.put("message", publishHomeworkResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
