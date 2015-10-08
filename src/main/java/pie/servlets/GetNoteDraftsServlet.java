package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
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
		out.println("{\"notes\" : [");
		for (int index = 0; index < noteDrafts.length; index++) {

			Note note = noteDrafts[index];
			out.println("{\"noteID\" : " + note.getNoteID() + ",");
			out.println("\"noteTitle\" : \"" + note.getNoteTitle() + "\",");
			out.println("\"noteDescription\" : \"" + note.getNoteDescription() + "\",");
			out.println("\"noteAuthor\" : \"" + note.getNoteAuthor().getUserFullName() + "\",");
			out.println("\"noteResponseQuestionID\" : " + note.getNoteQuestionID().getResponseQuestionID() + "}");
		}
		out.println("]}");
	}
}
