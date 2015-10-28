package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
import pie.Staff;
import pie.UserHomework;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

@Singleton
public class GetIndividualStudentReportServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5786385179931407941L;

	UserHomeworkService userHomeworkService;

	@Inject
	public GetIndividualStudentReportServlet(UserHomeworkService userHomeworkService) {
		this.userHomeworkService = userHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int studentID = 0;

		try {
			Map<String, String> requestParams = Utilities.getParameters(request, "studentID");
			studentID = Integer.parseInt(requestParams.get("studentID"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserHomework[] listHomework = userHomeworkService.getAllMarkedUserHomework(studentID);
		JSONObject responseObject = new JSONObject();
		JSONArray listGradedHomework = new JSONArray();
		for (UserHomework uh : listHomework) {
			JSONObject jsonUH = new JSONObject();
			GroupHomework groupHomework = userHomeworkService.getGroupHomework(uh.getUserHomeworkID(),
					uh.getHomework().getHomeworkID());
			Staff staff = userHomeworkService.getUserHomeworkPublisher(uh.getUserHomeworkID());
			

			Date startDate = groupHomework.getPublishDate();
			Date endDate = groupHomework.getTargetMarkingCompletionDate();
			long diff = endDate.getTime() - startDate.getTime();
			int daysTaken = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			String grade = uh.getGrade();
			
			jsonUH.put("userHomeworkID", uh.getUserHomeworkID());
			jsonUH.put("startDate", startDate.getTime());
			jsonUH.put("author", staff.getUserFullName());
			jsonUH.put("endDate", endDate.getTime());
			jsonUH.put("daysTaken", daysTaken);
			jsonUH.put("grade", grade);
			listGradedHomework.put(jsonUH);
		}

		responseObject.put("studentGrades", listGradedHomework);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
