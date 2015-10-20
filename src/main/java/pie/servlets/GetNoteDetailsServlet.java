package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Note;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetNoteDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = -1339342818451284421L;
	
	NoteService noteService;
	
	@Inject
	public GetNoteDetailsServlet(NoteService noteService) {
		this.noteService = noteService;
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
		
		responseObject.put("debug", note);
		if(note != null) {
			String publishDateStr = new SimpleDateFormat("dd-MM-yyyy").format(note.getDateCreated());
			String fullPublishDateStr = new SimpleDateFormat("dd MMMM yyyy").format(note.getDateCreated());
			
			responseObject.put("noteID", note.getNoteID());
			responseObject.put("noteTitle", note.getTitle());
			responseObject.put("noteDescription", note.getDescription());
			responseObject.put("noteAuthor", note.getStaff().getUserFullName());
			responseObject.put("noteResponseQuestionID", note.getResponseQuestion().getResponseQuestionID());
			responseObject.put("noteDateCreated", publishDateStr);
			responseObject.put("noteFullDateCreated", fullPublishDateStr);
						
		} else {
			
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Note not found");
			
		}
		
		out.write(responseObject.toString());
		
	}
	
}
