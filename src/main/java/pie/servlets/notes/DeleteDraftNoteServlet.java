package pie.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.NoteAttachmentService;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeleteDraftNoteServlet extends HttpServlet {

	private static final long serialVersionUID = 1989917926801430744L;

	NoteService noteService;
	NoteAttachmentService noteAttachmentService;

	@Inject
	public DeleteDraftNoteServlet(NoteService noteService, NoteAttachmentService noteAttachmentService) {
		this.noteService = noteService;
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noteID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "noteID");
			noteID = Integer.parseInt(requestParameters.get("noteID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		boolean isDeleted = noteService.deleteDraftNote(noteID);
		JSONObject responseObject = new JSONObject();

		String noteAttachmentURL = noteAttachmentService.getNoteAttachmentURL(noteID);
		noteAttachmentService.deleteNoteAttachment(noteAttachmentURL);

		if(isDeleted == true) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Draft note successfully deleted");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Draft note is not deleted");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
