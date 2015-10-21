package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NoteUnArchiveServlet extends HttpServlet{

	private static final long serialVersionUID = -1320416121525839532L;

	UserNoteService userNoteService;
	
	@Inject
	public NoteUnArchiveServlet(UserNoteService userNoteService) {
		this.userNoteService = userNoteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;
		int noteID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userID", "noteID");
			
			userID = Integer.parseInt(requestParameters.get("userID"));
			noteID = Integer.parseInt(requestParameters.get("noteID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		JSONObject responseObject = new JSONObject();
		
		if(userNoteService.unArchive(noteID, userID)) {
			responseObject.put("result", "SUCCESS");
			responseObject.put("message", "Note ID: " + noteID + " was un-archived by " + " UserID(" + userID + ")");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "Note was not successfully un-archived in DB");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
	
}