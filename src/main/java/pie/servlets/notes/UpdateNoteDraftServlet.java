package pie.servlets.notes;

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
public class UpdateNoteDraftServlet extends HttpServlet {

	private static final long serialVersionUID = -8898897563661464623L;

	NoteService noteService;
	
	@Inject
	public UpdateNoteDraftServlet(NoteService noteService) {
		this.noteService = noteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int noteID = 0;
		int responseQuestionID = 0;
		String noteTitle = null;
		String noteDescription = null;
		boolean isUpdated = false;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "noteID", "responseQuestionID", 
					"noteTitle", "noteDescription");

			noteID = Integer.parseInt(requestParameters.get("noteID"));
			responseQuestionID = Integer.parseInt(requestParameters.get("responseQuestionID"));
			noteTitle = requestParameters.get("noteTitle");
			noteDescription = requestParameters.get("noteDescription");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		isUpdated = noteService.updateNote(noteTitle, noteDescription, responseQuestionID, noteID);
		
		JSONObject responseObject = new JSONObject();
		
		if(isUpdated) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Draft note is updated");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Draft note is not updated");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
