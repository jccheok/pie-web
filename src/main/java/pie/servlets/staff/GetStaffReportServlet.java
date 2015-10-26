package pie.servlets.staff;

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
public class GetStaffReportServlet extends HttpServlet {

	/*
	 * author: cheok jia chin
	 * 
	 */

	private static final long serialVersionUID = 1182747967757433531L;

	GroupHomeworkService groupHomeworkService;

	@Inject
	public GetStaffReportServlet(GroupHomeworkService groupHomeworkService) {
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// get staff ID
		int staffID = 0;
		GroupHomework[] listSentHomework = {};

		try {
			// get request parameters
			Map<String, String> requestParams = Utilities.getParameters(request, "staffID");
			staffID = Integer.parseInt(requestParams.get("staffID"));
			listSentHomework = groupHomeworkService.getAllSentHomework(staffID);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		JSONArray listHomeworkEfforts = new JSONArray();
		for (GroupHomework gh : listSentHomework) {
			JSONObject effortReport = new JSONObject();
			int minutes = gh.getMarkingEffort();
			long diff = gh.getTargetMarkingCompletionDate().getTime() - gh.getDueDate().getTime();
			int daysTaken = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			int effortPerDay = minutes / daysTaken;
			effortReport.put("groupHomeworkID", gh.getGroupHomeworkID());
			effortReport.put("EffortPerDay", effortPerDay);
			effortReport.put("DaysTaken", daysTaken);
			effortReport.put("subject", gh.getHomework().getHomeworkSubject());
			effortReport.put("startDate", gh.getDueDate().getTime());
			effortReport.put("endDate", gh.getTargetMarkingCompletionDate().getTime());
			listHomeworkEfforts.put(effortReport);
		}

		responseObject.put("listEfforts", listHomeworkEfforts);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
