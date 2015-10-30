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
			noteID = Integer.parseInt(requestParameters.get("noteID"));
			
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
				userNoteObject.put("userNoteID", note.getUserNoteID());
				userNoteObject.put("userName", note.getUser().getUserFullName());
				userNoteObject.put("noteIsRead", note.isRead() ? true : false);
				userNoteObject.put("userResponse", note.getResponseText());
				
				int responseOptionID = note.getResponseOption().getResponseOptionID();
				
				switch(responseOptionID) {
				case 1: userNoteObject.put("responseOptionID", "Approve");
				break;
				case 2: userNoteObject.put("responseOptionID", "Reject");
				break;
				case 3: userNoteObject.put("responseOptionID", "Acknowledged");
				break;
				case 4: userNoteObject.put("responseOptionID", "Not Acknowledged");
				break;
				case 5: userNoteObject.put("responseOptionID", "Yes");
				break;
				case 6: userNoteObject.put("responseOptionID", "No");
				break;
				case 7: userNoteObject.put("responseOptionID", "No Response");
				break;
				
				}
				
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
