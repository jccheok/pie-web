package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.PublishNoteResult;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SendNoteServlet extends HttpServlet {

	private static final long serialVersionUID = -4985014150620092494L;

	NoteService noteService;
	
	@Inject
	public SendNoteServlet(NoteService noteService) {
		this.noteService = noteService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noteID = 0;
		int staffID = 0;
		int groupID = 0;
		int responseQuestionID = 0;
		String noteTitle = null;
		String noteDescription = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID", "responseQuestionID", 
					"noteTitle", "noteDescription");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			responseQuestionID = Integer.parseInt(requestParameters.get("responseQuestionID"));
			noteTitle = requestParameters.get("noteTitle");
			noteDescription = requestParameters.get("noteDescription");	


		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		noteID = noteService.createNote(staffID, responseQuestionID, noteTitle, noteDescription);
		JSONObject responseObject = new JSONObject();
		
		if(noteID != -1) {

			PublishNoteResult publishNoteResult = noteService.publishNote(noteID, groupID, staffID);
			responseObject.put("result", publishNoteResult.toString());
			responseObject.put("message", publishNoteResult.getDefaultMessage());
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Note is not created");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
