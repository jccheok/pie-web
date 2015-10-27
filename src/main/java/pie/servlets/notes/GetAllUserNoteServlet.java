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

import pie.GroupNote;
import pie.UserNote;
import pie.services.GroupNoteService;
import pie.services.NoteAttachmentService;
import pie.services.UserNoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllUserNoteServlet extends HttpServlet{

	private static final long serialVersionUID = 8471980891428507946L;

	UserNoteService userNoteService;
	GroupNoteService groupNoteService;
	NoteAttachmentService noteAttachmentService;
	
	@Inject
	public GetAllUserNoteServlet(UserNoteService userNoteService, GroupNoteService groupNoteService, NoteAttachmentService noteAttachmentService) {
		this.userNoteService = userNoteService;
		this.groupNoteService = groupNoteService;
		this.noteAttachmentService = noteAttachmentService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userID = 0;
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "userID");
			userID = Integer.parseInt(requestParameters.get("userID"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		UserNote[] allUserNote = userNoteService.getAllUserNote(userID);
		
		JSONObject responseObject = new JSONObject();
		
		if(allUserNote != null) {
			
			JSONArray noteList = new JSONArray();
			
			for(UserNote userNote : allUserNote) {
				
				JSONObject noteObject = new JSONObject();
				
				noteObject.put("noteID", userNote.getNote().getNoteID());
				noteObject.put("userNoteID", userNote.getUserNoteID());
				noteObject.put("noteTitle", userNote.getNote().getTitle());
				
				int noteDescriptionLength = userNote.getNote().getDescription().length();
				if(noteDescriptionLength > 15) {
					String noteShortDescription = Utilities.parseHtml(userNote.getNote().getDescription());
					noteObject.put("noteDescription", noteShortDescription);
				} else {
					noteObject.put("noteDescription", userNote.getNote().getDescription());
				}
				
				noteObject.put("publisherName", userNote.getNote().getStaff().getUserFullName());
				GroupNote groupNote = groupNoteService.getGroupNote(userNote.getUserNoteID(), userNote.getNote().getNoteID());
				noteObject.put("publishedDate", Utilities.parseServletDateFormat(groupNote.getPublishDate()));
				
				String noteAttachmentURL = noteAttachmentService.getNoteAttachmentURL(userNote.getNote().getNoteID());
				if(noteAttachmentURL != null) {
					noteObject.put("hasAttachment", true);
				} else {
					noteObject.put("hasAttachment", false);
				}
				
				noteList.put(noteObject);
				
			}
			
			responseObject.put("allUserNote", noteList);
			
		} else {
			responseObject.put("result", "No Sent Note");
			responseObject.put("message", "No Note was sent to this user");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
		
}
