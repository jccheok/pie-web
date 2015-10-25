package pie.servlets.notes;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SetNoteResponseServlet extends HttpServlet{

	private static final long serialVersionUID = -1785157719558724743L;

	UserNoteService userNoteService;
	
	@Inject
	public SetNoteResponseServlet(UserNoteService userNoteService) {
		this.userNoteService = userNoteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userNoteID = -1;
		int responseOptionID = -1;
		String responseText = null;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userNoteID", "responseOptionID", "responseText");
			
			userNoteID = Integer.parseInt(requestParameters.get("userNoteID"));
			responseOptionID = Integer.parseInt(requestParameters.get("responseOptionID"));
			responseText = requestParameters.get("responseText");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		userNoteService.setUserNoteResponse(userNoteID, responseOptionID, responseText);
		
	}
	
}
