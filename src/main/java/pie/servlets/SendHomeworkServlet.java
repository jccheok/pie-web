package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.constants.PublishHomeworkResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SendHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = -1127036563465130540L;

	HomeworkService homeworkService;
	GroupService groupService;

	@Inject
	public SendHomeworkServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		int staffID = 0;
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
					"homeworkDueDate","homeworkIsGraded");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			homeworkTitle = requestParameters.get("homeworkTitle");
			homeworkSubject = requestParameters.get("homeworkSubject");
			homeworkDescription = requestParameters.get("homeworkDescription");
			homeworkMinutesRequired = Integer.parseInt(requestParameters.get("homeworkMinutesRequired"));
			homeworkDueDate = dateFormat.parse(requestParameters.get("homeworkDueDate"));
			homeworkIsGraded = Integer.parseInt(requestParameters.get("homeworkIsGraded")) == 1 ? true:false;

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		int homeworkID = homeworkService.createHomework(staffID, homeworkTitle, homeworkSubject,
				homeworkDescription, homeworkMinutesRequired, homeworkDescription);

		JSONObject responseObject = new JSONObject();

		if (homeworkID != -1) {
			PublishHomeworkResult publishHomeworkResult = homeworkService.publishHomework(groupID, homeworkID, staffID);
			responseObject.put("result", publishHomeworkResult.toString());
			responseObject.put("message", publishHomeworkResult.getDefaultMessage());
		} else {
			responseObject.put("result", GenericResult.FAILED.toString());
			responseObject.put("message", "Failed to create homework");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
