package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MarkHomeworkServlet extends HttpServlet{
	
	private static final long serialVersionUID = -2578705910597182904L;
	
	UserHomeworkService userHomeworkService;
	
	@Inject
	public MarkHomeworkServlet(UserHomeworkService userHomeworkService){
		this.userHomeworkService = userHomeworkService;
	}
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String rawStudentList = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "rawStudentList");
			rawStudentList = requestParameters.get("rawStudentList");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject requestObject = new JSONObject(rawStudentList);
		JSONArray studentList = requestObject.getJSONArray("studentList");

		for (int index = 0; index < studentList.length(); index++) {

			JSONObject student = studentList.getJSONObject(index);

			int studentID = student.getInt("studentID");
			int userHomeworkID = student.getInt("userHomeworkID");

			userHomeworkService.markHomework(userHomeworkID, studentID);
		}

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", GenericResult.SUCCESS.toString());
		responseObject.put("message", "Successfully marked homework.");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
