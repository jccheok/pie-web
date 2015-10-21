package pie.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pie.services.NoteService;
import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NoteIsReadServlet extends HttpServlet {

	private static final long serialVersionUID = 6267658636515588161L;

	NoteService noteService;
	UserNoteService userNoteService;
	
	@Inject
	public NoteIsReadServlet(NoteService noteService) {
		this.noteService = noteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;
		int noteID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userID", "noteID");
			
			userID = Integer.parseInt(requestParameters.get("userID"));
			noteID = Integer.parseInt(requestParameters.get("noteID"));
			
		} catch (Exception e ) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		userNoteService.isRead(userNoteID)
		
	}
	
}
