package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.GenericResult;
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
	DateFormat dateFormat;

	@Inject
	public SendHomeworkServlet(HomeworkService homeworkService, GroupService groupService) {
		this.homeworkService = homeworkService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.dateFormat = new SimpleDateFormat("YY-mm-dd hh:mm:ss");

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

		int homeworkID = homeworkService.createHomework(staffID, groupID, homeworkTitle, homeworkSubject,
				homeworkDescription, homeworkMinutesRequired, homeworkDueDate);

		JSONObject responseObject = new JSONObject();

		if (homeworkID != -1) {
			homeworkService.sendHomework(groupID, homeworkID);
			responseObject.put("result", GenericResult.SUCCESS.toString());
			responseObject.put("message", "Homework successfully created & sent");
		} else {
			responseObject.put("result", GenericResult.FAILED.toString());
			responseObject.put("message", "Failed to create homework");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
