package pie.servlets;

import java.io.IOException;
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
public class GetIndividualStudentReport extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5786385179931407941L;

	UserHomeworkService userHomeworkService;

	@Inject
	public GetIndividualStudentReport(UserHomeworkService userHomeworkService) {
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
			Staff staff = userHomeworkService.getUserHomeworkPublisher(uh.getUserHomeworkID());
			GroupHomework groupHomework = userHomeworkService.getGroupHomework(uh.getUserHomeworkID(),
					uh.getHomework().getHomeworkID());

			Date startDate = groupHomework.getPublishDate();
			Date endDate = groupHomework.getTargetMarkingCompletionDate();
			long diff = endDate.getTime() - startDate.getTime();
			int daysTaken = (int) TimeUnit.DAYS.convert(diff, TimeUnit.DAYS);
			String grade = uh.getGrade();

			jsonUH.put("startDate", startDate.getTime());
			jsonUH.put("endDate", endDate.getTime());
			jsonUH.put("daysTaken", daysTaken);
			jsonUH.put("grade", grade);
			listGradedHomework.put(jsonUH);
		}

		responseObject.put("studentGrades", listGradedHomework);

	}
}
