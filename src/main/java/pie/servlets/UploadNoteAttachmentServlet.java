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

import pie.services.NoteAttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@MultipartConfig(location = "/var/lib/openshift/560246382d52714ebe00004d/app-root/data", fileSizeThreshold = 1024*1024*2, maxFileSize = 1024*1024*10, maxRequestSize = 1024*1024*50)
public class UploadNoteAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 6641714211325006810L;
	NoteAttachmentService noteAttachmentService;

	@Inject
	public UploadNoteAttachmentServlet(NoteAttachmentService noteAttachmentService) {
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();

		if(noteAttachmentService.checkIfNoteFolderExist()) {
			responseObject.put("Debug Log", "Note Folder exist");
		} else {
			responseObject.put("Debug Log", "Note Folder did not exist but was created during the process");
		}

		if(request.getParts() != null) {
			
			for(Part part : request.getParts()) {
				String noteAttachmentURL = noteAttachmentService.getNoteFileName(part);
				int noteAttachmentID = noteAttachmentService.createNoteAttachment(noteAttachmentURL);
				noteAttachmentURL = noteAttachmentService.updateNoteAttachmentName(noteAttachmentID, noteAttachmentURL);

				part.write(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
				responseObject.put("noteAttachmentID", noteAttachmentID);
				responseObject.put("noteAttachmentURL", noteAttachmentURL);
			}
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "No note file is uploaded");
		}

		out.println(responseObject);
	}
}
