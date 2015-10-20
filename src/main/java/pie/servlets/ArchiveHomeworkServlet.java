package pie.servlets;

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
public class ArchiveHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -8069236231768601771L;
	
	UserHomeworkService userHomeworkService;
	
	@Inject
	public ArchiveHomeworkServlet(UserHomeworkService userHomeworkService) {
		this.userHomeworkService = userHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userHomeworkID = 0;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userHomeworkID");

			userHomeworkID = Integer.parseInt(requestParameters.get("userHomeworkID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		boolean archiveResult = userHomeworkService.archiveHomework(userHomeworkID);
		JSONObject responseObject = new JSONObject();
		
		if(archiveResult){
			responseObject.put("result", "Success");
			responseObject.put("message", "Successfully archived the homework");
		}else{
			responseObject.put("result","Failure");
			responseObject.put("message", "Failed to archive homework");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
