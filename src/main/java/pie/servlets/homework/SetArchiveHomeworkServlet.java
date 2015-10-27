package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SetArchiveHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -8069236231768601771L;
	
	UserHomeworkService userHomeworkService;
	
	@Inject
	public SetArchiveHomeworkServlet(UserHomeworkService userHomeworkService) {
		this.userHomeworkService = userHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userHomeworkID = 0;
		boolean isArchived = false;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userHomeworkID", "isArchived");

			userHomeworkID = Integer.parseInt(requestParameters.get("userHomeworkID"));
			isArchived = Integer.parseInt(requestParameters.get("isArchived")) == 1;

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		GenericResult result = userHomeworkService.archiveHomework(userHomeworkID, isArchived) ? GenericResult.SUCCESS : GenericResult.FAILED;
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", result.toString());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
