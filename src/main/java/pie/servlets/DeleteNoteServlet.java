package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeleteNoteServlet extends HttpServlet {

	private static final long serialVersionUID = -5578757019899377191L;

	NoteService noteService;
	
	@Inject
	public DeleteNoteServlet(NoteService noteService) {
		this.noteService = noteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int noteID = 0;
		boolean deleteResult = false;
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "noteID");

			noteID = Integer.parseInt(requestParameters.get("noteID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		
		deleteResult = noteService.deleteNote(noteID);
		
		JSONObject responseObject = new JSONObject();
		if(deleteResult) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Note deleted successfully");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message","Note is not deleted");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
}
