package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import pie.services.HomeworkAttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@MultipartConfig(location = "/var/lib/openshift/560246382d52714ebe00004d/app-root/data", fileSizeThreshold = 1024*1024*2, maxFileSize = 1024*1024*10, maxRequestSize = 1024*1024*50)
public class UploadHomeworkAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = -3935192767647779004L;

	HomeworkAttachmentService homeworkAttachmentService;

	@Inject
	public UploadHomeworkAttachmentServlet(HomeworkAttachmentService homeworkAttachmentService) {
		this.homeworkAttachmentService = homeworkAttachmentService; 
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();
		
		if(homeworkAttachmentService.checkIfHomeworkFolderExist()) {
			responseObject.put("Debug Log", "Homework Folder exist");
		} else {
			responseObject.put("Folder", "Homework Folder did not exist but was created during the process");
		}
		
		if(request.getParts() != null) {
			
			for(Part part : request.getParts()) {
				String homeworkAttachmentURL = homeworkAttachmentService.getHomeworkFileName(part);
				int homeworkAttachmentID = homeworkAttachmentService.createHomeworkAttachment(homeworkAttachmentURL);
				homeworkAttachmentURL = homeworkAttachmentService.updateHomeworkAttachmentName(homeworkAttachmentID, homeworkAttachmentURL);
		
				part.write(homeworkAttachmentService.getHomeworkAttachmentDIR(homeworkAttachmentURL));
				responseObject.put("homeworkAttachmentID", homeworkAttachmentID);
				responseObject.put("homeworkAttachmentURL", homeworkAttachmentURL);
			}
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "No homework file is uploaded");
		}

		out.println(responseObject);
	}
}
