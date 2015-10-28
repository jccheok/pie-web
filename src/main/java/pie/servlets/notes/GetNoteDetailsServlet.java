package pie.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.GroupNote;
import pie.Note;
import pie.services.GroupNoteService;
import pie.services.NoteAttachmentService;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetNoteDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = -1339342818451284421L;

	NoteService noteService;
	NoteAttachmentService noteAttachmentService;
	GroupNoteService groupNoteService;

	@Inject
	public GetNoteDetailsServlet(NoteService noteService, NoteAttachmentService noteAttachmentService, GroupNoteService groupNoteService) {
		this.noteService = noteService;
		this.noteAttachmentService = noteAttachmentService;
		this.groupNoteService = groupNoteService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noteID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "noteID");

			noteID = Integer.parseInt(requestParameters.get("noteID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Note note = noteService.getNote(noteID);
		PrintWriter out = response.getWriter();
		JSONObject responseObject = new JSONObject();

		if(note != null) {

			responseObject.put("noteID", note.getNoteID());
			responseObject.put("noteTitle", note.getTitle());
			responseObject.put("noteDescription", note.getDescription());
			responseObject.put("noteAuthor", note.getStaff().getUserFullName());
			responseObject.put("noteResponseQuestionID", note.getResponseQuestion().getResponseQuestionID());
			responseObject.put("noteDateCreated", Utilities.parseServletDateFormat(note.getDateCreated()));

			String noteAttachmentURL = noteAttachmentService.getNoteAttachmentURL(note.getNoteID());
			if(noteAttachmentURL != null) {
				responseObject.put("noteAttachmentURL", noteAttachmentURL);
			}

			int groupNoteID = groupNoteService.getGroupNotes(note.getNoteID());
			if(groupNoteID != -1) {
				GroupNote groupNote = groupNoteService.getGroupNote(groupNoteID);
				responseObject.put("groupName", groupNote.getGroup().getGroupName());
			}

		} else {

			responseObject.put("result", "FAILED");
			responseObject.put("message", "Note not found");

		}

		out.write(responseObject.toString());

	}

}
