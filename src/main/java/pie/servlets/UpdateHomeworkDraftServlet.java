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

import pie.constants.UpdateHomeworkDraftResult;
import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class UpdateHomeworkDraftServlet extends HttpServlet {

	private static final long serialVersionUID = 2084193448839062138L;

	HomeworkService homeworkService;
	GroupService groupService;

	@Inject
	public UpdateHomeworkDraftServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
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
			Map<String, String> requestParameters = Utilities.getParameters(request, "homeworkID", "groupID",
					"homeworkTitle", "homeworkSubject", "homeworkDescription", "homeworkMinutesRequired",
					"homeworkDueDate", "homeworkIsGraded");

			homeworkID = Integer.parseInt(requestParameters.get("homeworkID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			homeworkTitle = requestParameters.get("homeworkTitle");
			homeworkSubject = requestParameters.get("homeworkSubject");
			homeworkDescription = requestParameters.get("homeworkDescription");
			homeworkDueDate = dateFormat.parse(requestParameters.get("homeworkDueDate"));
			homeworkIsGraded = Integer.parseInt(requestParameters.get("homeworkIsGraded")) == 1 ? true : false;

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		
		UpdateHomeworkDraftResult updateHomeworkDraftResult = homeworkService.updateDraftHomework(homeworkID, homeworkTitle, homeworkSubject, homeworkDescription, homeworkMinutesRequired, homeworkDueDate, homeworkIsGraded);
		
		responseObject.put("result", updateHomeworkDraftResult.toString());
		responseObject.put("message",updateHomeworkDraftResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
