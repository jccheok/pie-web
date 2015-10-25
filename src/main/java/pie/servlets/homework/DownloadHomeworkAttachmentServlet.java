package pie.servlets.homework;

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

import pie.services.HomeworkAttachmentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DownloadHomeworkAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 8015672858794411554L;

	HomeworkAttachmentService homeworkAttachmentService;

	@Inject
	public DownloadHomeworkAttachmentServlet(HomeworkAttachmentService homeworkAttachmentService) {
		this.homeworkAttachmentService = homeworkAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String homeworkAttachmentURL = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "homeworkAttachmentURL");
			homeworkAttachmentURL = requestParameters.get("homeworkAttachmentURL");

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		File downloadFile = new File(homeworkAttachmentService.getHomeworkAttachmentDIR(homeworkAttachmentURL));
		FileInputStream inStream = new FileInputStream(downloadFile);

		ServletContext context = getServletContext();

		String mimeType = context.getMimeType(homeworkAttachmentService.getHomeworkAttachmentDIR(homeworkAttachmentURL));
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