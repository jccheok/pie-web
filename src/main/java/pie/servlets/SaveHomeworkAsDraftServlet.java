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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.services.GroupService;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class SaveHomeworkAsDraftServlet extends HttpServlet {

	private static final long serialVersionUID = -4173405562367255160L;

	HomeworkService homeworkService;
	GroupService groupService;

	@Inject
	public SaveHomeworkAsDraftServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		DateFormat dateFormat = new SimpleDateFormat("YY-mm-dd hh:mm:ss");

		int staffID = 0;
		int groupID = 0;
		String homeworkTitle = null;
		String homeworkSubject = null;
		String homeworkDescription = null;
		int homeworkMinutesRequired = 0;
		Date homeworkDueDate = null;

		try {
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID",
					"homeworkTitle", "homeworkSubject", "homeworkDescription", "homeworkMinutesRequired",
					"homeworkDueDate");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			homeworkTitle = requestParameters.get("homeworkTitle");
			homeworkSubject = requestParameters.get("homeworkSubject");
			homeworkDescription = requestParameters.get("homeworkDescription");
			homeworkMinutesRequired = Integer.parseInt(requestParameters.get("homeworkMinutesRequired"));
			homeworkDueDate = new Date(dateFormat.parse(requestParameters.get("homeworkDueDate")).getTime());

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject responseObject = new JSONObject();

		int homeworkID = homeworkService.createHomework(staffID, groupID, homeworkTitle, homeworkSubject,
				homeworkDescription, homeworkMinutesRequired, homeworkDueDate);

		if (homeworkID != -1) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Homework successfully created as draft");
		} else {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Failed to create homework");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
