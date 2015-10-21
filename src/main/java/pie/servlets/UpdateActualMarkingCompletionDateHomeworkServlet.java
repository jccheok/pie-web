package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.GroupHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class UpdateActualMarkingCompletionDateHomeworkServlet {
	
	GroupHomeworkService groupHomeworkService;

	@Inject
	public UpdateActualMarkingCompletionDateHomeworkServlet(GroupHomeworkService groupHomeworkService) {
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int groupHomeworkID = 0;

		try {
			Map<String, String> requestParameters = Utilities.getParameters(request, "groupHomeworkID");

			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean updateResult = groupHomeworkService.updateActualMarkingCompletion(groupHomeworkID);

		JSONObject responseObject = new JSONObject();

		if (updateResult) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Successfully updated actual marking completion date of homework.");
		} else {
			responseObject.put("result", "FAILURE");
			responseObject.put("message", "Failed to update actual marking completion date of homework.");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
	
}
