package pie.servlets;

import java.io.IOException;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		int homeworkID = 0;
		int groupID = 0;
		String homeworkTitle = null;
		String homeworkSubject = null;
		String homeworkDescription = null;
		int homeworkMinutesRequired = 0;
		Date homeworkDueDate = null;
		boolean homeworkIsGraded = false;

		try {
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID",
					"homeworkTitle", "homeworkSubject", "homeworkDescription", "homeworkMinutesRequired",
					"homeworkDueDate", "homeworkIsGraded");

			groupID = Integer.parseInt(requestParameters.get("groupID"));
			homeworkTitle = requestParameters.get("homeworkTitle");
			homeworkSubject = requestParameters.get("homeworkSubject");
			homeworkDescription = requestParameters.get("homeworkDescription");
			homeworkMinutesRequired = Integer.parseInt(requestParameters.get("homeworkMinutesRequired"));
			homeworkDueDate = dateFormat.parse(requestParameters.get("homeworkDueDate"));
			homeworkIsGraded = Integer.parseInt(requestParameters.get("homeworkIsGraded")) == 1 ? true : false;
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		if (homeworkService.updateDraftHomework(homeworkID, homeworkTitle, homeworkSubject, homeworkDescription,
				homeworkMinutesRequired, homeworkDueDate, homeworkIsGraded)) {
			PublishHomeworkResult publishHomeworkResult = homeworkService.publishHomework(groupID, homeworkID);
			responseObject.put("result", publishHomeworkResult.toString());
			responseObject.put("message", publishHomeworkResult.getDefaultMessage());
		} else {
			responseObject.put("result", "Failure");
			responseObject.put("message", "Fail to update homework");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
