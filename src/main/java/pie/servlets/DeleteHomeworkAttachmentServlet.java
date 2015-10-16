package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.HomeworkAttachmentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeleteHomeworkAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 8070651806066866278L;
	
	HomeworkAttachmentService homeworkAttachmentService;
	
	@Inject
	public DeleteHomeworkAttachmentServlet(HomeworkAttachmentService homeworkAttachmentService) {
		this.homeworkAttachmentService = homeworkAttachmentService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String homeworkAttachmentURL = null;
		
		JSONObject responseObject = new JSONObject();
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "homeworkAttachmentURL");
			homeworkAttachmentURL = requestParameters.get("homeworkAttachmentURL");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		if(homeworkAttachmentService.deleteHomeworkAttachment(homeworkAttachmentURL)) {
			responseObject.put("result", "SUCCESSFUL");
			responseObject.put("message", homeworkAttachmentURL + " is deleted");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", homeworkAttachmentURL + " is not deleted");
		}
			
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
