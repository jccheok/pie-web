package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SetAcknowledgeHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = -7482027440716588670L;
	
	UserHomeworkService userHomeworkService;

	@Inject
	public SetAcknowledgeHomeworkServlet(UserHomeworkService userHomeworkService) {
		this.userHomeworkService = userHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int userHomeworkID = 0;
		boolean isAcknowledged = false;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userHomeworkID", "isAcknowledged");

			userHomeworkID = Integer.parseInt(requestParameters.get("userHomeworkID"));
			isAcknowledged = Integer.parseInt(requestParameters.get("isAcknowledged")) == 1;

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		boolean acknowledgeResult = userHomeworkService.setAcknowledged(userHomeworkID, isAcknowledged);
		JSONObject responseObject = new JSONObject();

		if (acknowledgeResult) {
			responseObject.put("result", "Success");
			responseObject.put("message", "Successfully archived the homework");
		} else {
			responseObject.put("result", "Failure");
			responseObject.put("message", "Failed to archive homework");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
