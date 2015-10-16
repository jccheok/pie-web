package pie.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.utilities.Utilities;

import com.google.inject.Singleton;

@Singleton
public class DeleteHomeworkAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 8070651806066866278L;
	
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
		
		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
		String uploadDir = uploadPath + File.separator + "uploadFiles" + "/" + homeworkAttachmentURL;
		
		File fileSaveDir = new File(uploadDir);
		if(!fileSaveDir.exists()) {
			fileSaveDir.delete();
			
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
