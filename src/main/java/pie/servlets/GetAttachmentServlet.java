package pie.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pie.services.AttachmentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 404605948552377058L;

	AttachmentService attachmentService;

	@Inject
	public GetAttachmentServlet(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileURL = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "fileURL");
			fileURL = requestParameters.get("fileURL");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		String uploadPath = System.getenv("OPENSHIFT_DATA_DIR") + "uploadFiles/" + fileURL;
		File downloadFile = new File(uploadPath);
		FileInputStream inStream = new FileInputStream(downloadFile);

		ServletContext context = getServletContext();

		String mimeType = context.getMimeType(uploadPath);
		if (mimeType == null) {        
			mimeType = "application/octet-stream";
		}

		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inStream.close();
		outStream.close();     
	}
}