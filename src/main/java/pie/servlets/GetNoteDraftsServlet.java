package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Note;
import pie.services.NoteService;
import pie.utilities.Utilities;

@Singleton
public class GetNoteDraftsServlet extends HttpServlet{

	private static final long serialVersionUID = -5614424479603279767L;
	
	NoteService noteService;
	
	@Inject
	public GetNoteDraftsServlet(NoteService noteService) {
		this.noteService = noteService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		Note[] noteDrafts = null;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		noteDrafts = noteService.getNoteDrafts(staffID);
		JSONObject responseObject = new JSONObject();
		JSONArray noteList = new JSONArray();
		if(noteDrafts != null) {
			for (Note note : noteDrafts) {
				JSONObject noteObject = new JSONObject();
				noteObject.put("noteID", note.getNoteID());
				noteObject.put("noteTitle", note.getNoteTitle());
				noteObject.put("noteDescription", note.getNoteDescription());
				noteObject.put("noteAuthor", note.getNoteAuthor().getUserFullName());
				noteObject.put("noteResponseQuestionID", note.getNoteQuestionID().getResponseQuestionID());
				noteList.put(noteObject);
			}
			responseObject.put("draftNote", noteList);
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Draft note not found");
		}
		

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
