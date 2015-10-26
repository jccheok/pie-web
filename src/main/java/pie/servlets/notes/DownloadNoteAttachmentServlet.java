package pie.servlets.notes;

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

import pie.services.NoteAttachmentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DownloadNoteAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 5561911532605799305L;
	NoteAttachmentService noteAttachmentService;

	@Inject
	public DownloadNoteAttachmentServlet(NoteAttachmentService noteAttachmentService) {
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String noteAttachmentURL = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "noteAttachmentURL");
			noteAttachmentURL = requestParameters.get("noteAttachmentURL");

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		File downloadFile = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
		FileInputStream inStream = new FileInputStream(downloadFile);

		ServletContext context = getServletContext();

		String mimeType = context.getMimeType(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
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