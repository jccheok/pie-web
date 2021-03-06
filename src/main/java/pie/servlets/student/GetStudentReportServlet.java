package pie.servlets.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.GroupHomework;
import pie.services.GroupHomeworkService;
import pie.utilities.Utilities;

@Singleton
public class GetStudentReportServlet extends HttpServlet {

	private static final long serialVersionUID = -3787718762646773038L;
	GroupHomeworkService groupHomeworkService;

	@Inject
	public GetStudentReportServlet(GroupHomeworkService groupHomeworkService) {
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int groupID = 0;
		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "groupID");
			groupID = Integer.parseInt(requestParams.get("groupID"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		GroupHomework[] listGh = groupHomeworkService.getSentHomeworkForGroup(groupID);

		JSONObject responseObject = new JSONObject();
		JSONArray listHomeworkEfforts = new JSONArray();
		for (GroupHomework gh : listGh) {
			JSONObject groupHomework = new JSONObject();
			long diff = gh.getDueDate().getTime() - gh.getPublishDate().getTime();
			double effortByStudent = gh.getHomework().gethomeworkMinutesReqStudent();
			int daysTaken = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			double effortPerDay = Math.round((effortByStudent / daysTaken)*100.0)/100.0;
			groupHomework.put("groupHomeworkID", gh.getGroupHomeworkID());
			groupHomework.put("EffortsPerDay", effortPerDay);
			groupHomework.put("DaysTaken", daysTaken);
			groupHomework.put("subject", gh.getHomework().getHomeworkSubject());
			groupHomework.put("startDate", Utilities.parseServletDateFormat(gh.getPublishDate()));
			groupHomework.put("endDate", Utilities.parseServletDateFormat(gh.getDueDate()));
			listHomeworkEfforts.put(groupHomework);
		}

		responseObject.put("listEfforts", listHomeworkEfforts);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}