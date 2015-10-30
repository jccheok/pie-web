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

import pie.Parent;
import pie.UserNote;
import pie.services.ParentStudentService;
import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetUserResponseServlet extends HttpServlet {

	private static final long serialVersionUID = 3902948437108392541L;

	UserNoteService userNoteService;
	ParentStudentService parentStudentService;
	
	@Inject
	public GetUserResponseServlet(UserNoteService userNoteService, ParentStudentService parentStudentService) {
		this.userNoteService = userNoteService;
		this.parentStudentService = parentStudentService;
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
				userNoteObject.put("childName", note.getUser().getUserFullName());
				userNoteObject.put("noteIsRead", note.isRead() ? true : false);
				userNoteObject.put("ResponseText", note.getResponseText());
				
				int responseOptionID = note.getResponseOption().getResponseOptionID();
				
				switch(responseOptionID) {
				case 1: userNoteObject.put("responseOption", "Approve");
				break;
				case 2: userNoteObject.put("responseOption", "Reject");
				break;
				case 3: userNoteObject.put("responseOption", "Acknowledged");
				break;
				case 4: userNoteObject.put("responseOption", "Not Acknowledged");
				break;
				case 5: userNoteObject.put("responseOption", "Yes");
				break;
				case 6: userNoteObject.put("responseOption", "No");
				break;
				case 7: userNoteObject.put("responseOption", "No Response");
				break;
				
				}
				
				String mobile = note.getUser().getUserMobile();
				userNoteObject.put("parentMobile", mobile);
				
				Parent parent = parentStudentService.getMainParent(note.getUserNoteID());
				if(parent != null) {
					userNoteObject.put("parentName", parent.getUserFullName());
				} else {
					userNoteObject.put("parentName", "-");
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
