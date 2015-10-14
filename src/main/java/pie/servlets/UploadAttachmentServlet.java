package pie.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;

import pie.services.AttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@MultipartConfig(location = "/var/lib/openshift/560246382d52714ebe00004d/app-root/data", fileSizeThreshold = 1024*1024*2, maxFileSize = 1024*1024*10, maxRequestSize = 1024*1024*50)
public class UploadAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 4834340741739873069L;

	AttachmentService attachmentService;

	@Inject
	public UploadAttachmentServlet(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();
		
		try {
		
		
        String uploadPath = System.getenv("OPENSHIFT_DATA_DIR");
        String uploadDir = uploadPath + File.separator + "uploadFiles";
         
        File fileSaveDir = new File(uploadDir);
        if(!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
         
        for(Part part : request.getParts()) {
            String fileName = attachmentService.getFileName(part);
            part.write(uploadDir + File.separator + fileName);
        }
        
        String debugLog = "Debug Log: " + uploadDir + " | " + request.getParts().toString();
        
        if(request.getParts() != null) {
        	
        	responseObject.put("result", "SUCCESS");
        	responseObject.put("message", "File is uploaded");
        	responseObject.put("debug", debugLog);
        } else {
        	responseObject.put("result", "FAILED");
        	responseObject.put("message", "No file is uploaded");
        	responseObject.put("debug", debugLog);
        }
	} catch (Exception e) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		return;

	}
  
        out.println(responseObject);
        
    }

}
