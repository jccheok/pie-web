package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.services.NoteAttachmentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeleteNoteAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 2448974350801163124L;

	NoteAttachmentService noteAttachmentService;
	
	@Inject
	public DeleteNoteAttachmentServlet(NoteAttachmentService noteAttachmentService) {
		this.noteAttachmentService = noteAttachmentService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String noteAttachmentURL = null;
		
		JSONObject responseObject = new JSONObject();
		
		try {
			
			Map<String, String> requestParameters = Utilities.getParameters(request, "noteAttachmentURL");
			noteAttachmentURL = requestParameters.get("noteAttachmentURL");
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		if(noteAttachmentService.deleteNoteAttachment(noteAttachmentURL)) {
			responseObject.put("result", "SUCCESSFUL");
			responseObject.put("message", noteAttachmentURL + " is deleted");
		} else {
			responseObject.put("result", "FAILED");
			responseObject.put("message", noteAttachmentURL + " is not deleted");
		}
			
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}
	
}
