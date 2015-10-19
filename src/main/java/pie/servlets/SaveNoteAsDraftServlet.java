package pie.servlets;

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
public class SaveNoteAsDraftServlet extends HttpServlet {

	private static final long serialVersionUID = 9157470052062586665L;

	NoteService noteService;
	NoteAttachmentService noteAttachmentService;

	@Inject
	public SaveNoteAsDraftServlet(NoteService noteService, NoteAttachmentService noteAttachmentService) {
		this.noteService = noteService;
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noteID = 0;
		int staffID = 0;
		int responseQuestionID = 0;
		int noteAttachmentID = 1;
		String noteTitle = null;
		String noteDescription = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "responseQuestionID", "noteAttachmentID", "noteTitle", "noteDescription");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			responseQuestionID = Integer.parseInt(requestParameters.get("responseQuestionID"));
			noteAttachmentID = Integer.parseInt(requestParameters.get("noteAttachmentID"));
			noteTitle = requestParameters.get("noteTitle");
			noteDescription = requestParameters.get("noteDescription");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		noteID = noteService.createNote(staffID, responseQuestionID, noteTitle, noteDescription);
		JSONObject responseObject = new JSONObject();

		if (noteID != -1) {
			
			if(noteAttachmentID != 1) {
				noteAttachmentService.UpdateNoteAttachmentID(noteAttachmentID, noteID);
			}
			
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Note is successfully created and saved as draft");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Note is not created!");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
