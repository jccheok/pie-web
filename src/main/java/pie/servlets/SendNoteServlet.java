package pie.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import pie.constants.PublishNoteResult;
import pie.services.NoteAttachmentService;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@MultipartConfig(location = "/var/lib/openshift/560246382d52714ebe00004d/app-root/data", fileSizeThreshold = 1024*1024*2, maxFileSize = 1024*1024*10, maxRequestSize = 1024*1024*50)
public class SendNoteServlet extends HttpServlet {

	private static final long serialVersionUID = -4985014150620092494L;
	private static final int maxRequestSize = 1024*1024*10;
	private static final int memorySize = 1024*1024*3;

	NoteService noteService;
	NoteAttachmentService noteAttachmentService;

	@Inject
	public SendNoteServlet(NoteService noteService, NoteAttachmentService noteAttachmentService) {
		this.noteService = noteService;
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noteID = 0;
		int staffID = 0;
		int groupID = 0;
		int responseQuestionID = 0;
		int noteAttachmentID = 1;
		String noteTitle = null;
		String noteDescription = null;
		String noteAttachmentURL = null;
		
		PrintWriter out = response.getWriter();

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID", "responseQuestionID", "noteTitle", "noteDescription");

			staffID = Integer.parseInt(requestParameters.get("staffID"));
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			responseQuestionID = Integer.parseInt(requestParameters.get("responseQuestionID"));
			noteTitle = requestParameters.get("noteTitle");
			noteDescription = requestParameters.get("noteDescription");	

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		JSONObject responseObject = new JSONObject();
		noteID = noteService.createNote(staffID, responseQuestionID, noteTitle, noteDescription);

		if(noteID != -1) {
		
			if(noteAttachmentService.checkIfNoteFolderExist()) {
				responseObject.put("folderResult", "Note Folder exist");
			} else {
				responseObject.put("folderResult", "Note Folder did not exist but was created during the process");
			}
		
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(memorySize);
			factory.setRepository(new File(System.getenv("OPENSHIFT_TMP_DIR")));

			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxRequestSize);
			
			try {

				List<FileItem> items = upload.parseRequest(request);
				
				if(items != null && items.size() > 0) {

					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem item = iter.next();

						if (!item.isFormField() && item.getSize() > 0) {

							noteAttachmentURL = new File(item.getName()).getName();
							noteAttachmentID = noteAttachmentService.createNoteAttachment(noteAttachmentURL, noteID);
							noteAttachmentURL = noteAttachmentService.updateNoteAttachmentName(noteAttachmentID, noteAttachmentURL);

							File storeFile = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
							item.write(storeFile);
							
							responseObject.put("fileResult", "SUCCESS");
							responseObject.put("noteAttachmentID", noteAttachmentID);
							responseObject.put("noteAttachmentURL", noteAttachmentURL);
							
						} else {
							responseObject.put("fileResult", "No file is uploaded / 0 bytes");
						}
					}
					
				} else {
					responseObject.put("uploadResult", "No item found");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			PublishNoteResult publishNoteResult = noteService.publishNote(noteID, groupID, staffID);
			responseObject.put("result", publishNoteResult.toString());
			responseObject.put("message", publishNoteResult.getDefaultMessage());

		} else {
			responseObject.put("noteResult", "Note was not created");
		}

		
		out.write(responseObject.toString());
	}
}
