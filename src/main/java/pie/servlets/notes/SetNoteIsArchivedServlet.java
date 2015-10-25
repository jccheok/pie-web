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
public class SetNoteIsArchivedServlet extends HttpServlet {

	private static final long serialVersionUID = -1295128666342415143L;
	
	UserNoteService userNoteService;
	
	@Inject
	public SetNoteIsArchivedServlet(UserNoteService userNoteService) {
		this.userNoteService = userNoteService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userNoteID = -1;
		boolean isArchived = false;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userNoteID", "isArchived");
			
			userNoteID = Integer.parseInt(requestParameters.get("userNoteID"));
			isArchived = Integer.parseInt(requestParameters.get("isArchived")) == 1;
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		userNoteService.setUserNoteArchived(userNoteID, isArchived);
	}
	
}
