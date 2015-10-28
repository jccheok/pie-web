package pie.servlets.notes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.GroupNote;
import pie.Note;
import pie.services.GroupNoteService;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllSentNotesServlet extends HttpServlet {

	private static final long serialVersionUID = 5429185538296554566L;

	NoteService noteService;
	GroupNoteService groupNoteService;
	
	@Inject
	public GetAllSentNotesServlet(NoteService noteService, GroupNoteService groupNoteService) {
		this.noteService = noteService;
		this.groupNoteService = groupNoteService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		Note[] notes = null;
		notes = noteService.getSentNotes(staffID);
		JSONObject responseObject = new JSONObject();
		JSONArray sentNoteList = new JSONArray();
		
		if(notes != null) {
			
			for (Note note : notes) {
								
				JSONObject noteObject = new JSONObject();
				noteObject.put("noteID", note.getNoteID());
				noteObject.put("noteTitle", note.getTitle());
				noteObject.put("noteDescription", note.getDescription());
				noteObject.put("noteAuthor", note.getStaff().getUserFullName());
				noteObject.put("noteResponseQuestionID", note.getResponseQuestion().getResponseQuestionID());
				noteObject.put("notePublishDate", note.getDateCreated());
				int groupNoteID = groupNoteService.getGroupNotes(note.getNoteID());
				GroupNote groupNote = groupNoteService.getGroupNote(groupNoteID);
				noteObject.put("groupName", groupNote.getGroup().getGroupName());
				sentNoteList.put(noteObject);
			}
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Sent Notes is not retrieved");
		}
		
		responseObject.put("sentNotes", sentNoteList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
		
	}
	
}
