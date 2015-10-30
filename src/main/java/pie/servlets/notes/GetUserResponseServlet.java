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

import pie.UserNote;
import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetUserResponseServlet extends HttpServlet {

	private static final long serialVersionUID = 3902948437108392541L;

	UserNoteService userNoteService;
	
	@Inject
	public GetUserResponseServlet(UserNoteService userNoteService) {
		this.userNoteService = userNoteService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int noteID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "noteID");
			noteID = Integer.parseInt(requestParameters.get("staffID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		UserNote[] allUserResponse = userNoteService.getUserNoteResponse(noteID);
		
		JSONObject responseObject = new JSONObject();
		JSONArray userResponseList = new JSONArray();
		
		if(allUserResponse != null) {
			
			for (UserNote note : allUserResponse) {
								
				JSONObject userNoteObject = new JSONObject();
				userNoteObject.put("userID", note.getUserNoteID());
				userNoteObject.put("noteIsRead", note.isRead() ? true : false);
				userNoteObject.put("userResponse", note.getResponseText());
				userNoteObject.put("responseOptionID", note.getResponseOption().getResponseOptionID());
				
				userResponseList.put(userNoteObject);
			}
			
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", "UserNote Responses is not retrieved");
		}
		
		responseObject.put("noteResponses", userResponseList);
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
	
}
