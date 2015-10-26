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
public class SetReadHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = 1143370964920298110L;

	UserHomeworkService userHomeworkService;
	
	@Inject
	public SetReadHomeworkServlet(UserHomeworkService userHomeworkService) {
		this.userHomeworkService = userHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userHomeworkID = 0;
		boolean isRead = false;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userHomeworkID", "isRead");

			userHomeworkID = Integer.parseInt(requestParameters.get("userHomeworkID"));
			isRead = Integer.parseInt(requestParameters.get("isRead")) == 1;

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		boolean readResult = userHomeworkService.readHomework(userHomeworkID, isRead);
		JSONObject responseObject = new JSONObject();
		
		if(readResult){
			responseObject.put("result", "Success");
			responseObject.put("message", "Successfully set homework to read");
		}else{
			responseObject.put("result","Failure");
			responseObject.put("message", "Failed to set the homework to read");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
