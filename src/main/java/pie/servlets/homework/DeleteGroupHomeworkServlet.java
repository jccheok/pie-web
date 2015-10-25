package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.GroupHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeleteGroupHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 4001648799535140886L;
	
	GroupHomeworkService groupHomeworkService;
	
	@Inject
	public DeleteGroupHomeworkServlet(GroupHomeworkService groupHomeworkService) {
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int groupHomeworkID = 0;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupHomeworkID");

			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		boolean deleteResult = groupHomeworkService.deleteSentHomework(groupHomeworkID);
		
		JSONObject responseObject = new JSONObject();
		if(deleteResult){
			responseObject.put("result", "Success");
			responseObject.put("message", "Successfully deleted the homework");
		}else{
			responseObject.put("result","Failure");
			responseObject.put("message", "Failed to delete homework");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
