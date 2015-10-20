package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class SubmitHomeworkServlet {

	UserHomeworkService userHomeworkService;
	
	@Inject
	public SubmitHomeworkServlet(UserHomeworkService userHomeworkService){
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
		GenericResult result = GenericResult.SUCCESS; 
		for (int index = 0; index < studentList.length(); index++) {

			JSONObject student = studentList.getJSONObject(index);

			int userHomeworkID = student.getInt("userHomeworkID");

			if(!userHomeworkService.submitHomework(userHomeworkID)){
				result = GenericResult.FAILED;
				break;
			}
		}

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", result);
		if(result == GenericResult.SUCCESS)
			responseObject.put("message", "Successfully submitted homework.");
		else
			responseObject.put("message", "Failed to submit homework.");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
