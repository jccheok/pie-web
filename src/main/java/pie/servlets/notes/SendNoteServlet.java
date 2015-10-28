package pie.servlets.notes;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import pie.constants.PublishNoteResult;
import pie.services.NoteAttachmentService;
import pie.services.NoteService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
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
		String groupIDList = null;
		int responseQuestionID = 0;
		int noteAttachmentID = 1;
		boolean fileDetected = false;
		boolean isDuplicate = false;
		String noteTitle = null;
		String noteDescription = null;
		String noteAttachmentURL = null;
		FileItem fileUpload = null;

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();

		noteAttachmentService.checkIfNoteFolderExist();

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

						fileDetected = true;
						fileUpload = item;

						noteAttachmentURL = new File(item.getName()).getName();

					} else {

						if(item.getFieldName().equalsIgnoreCase("staffID")) {
							staffID = Integer.parseInt(item.getString());
						} else if(item.getFieldName().equalsIgnoreCase("groupID")) {
							groupIDList = item.getString();
						} else if(item.getFieldName().equalsIgnoreCase("responseQuestionID")) {
							responseQuestionID = Integer.parseInt(item.getString());
						} else if(item.getFieldName().equalsIgnoreCase("noteTitle")) {
							noteTitle = Utilities.cleanHtml(item.getString());
						} else if(item.getFieldName().equalsIgnoreCase("noteDescription")) {
							noteDescription = Utilities.cleanHtml(item.getString());
						}
					} 
				}
			} else {
				responseObject.put("Field-Result", "FAILED");
				responseObject.put("Field-Message", "No/certain form is not filled");
			}

			if(staffID != 0 && responseQuestionID != 0) {

				JSONObject requestObject = new JSONObject(groupIDList);
				JSONArray groupList = requestObject.getJSONArray("groupIDArray");

				for (int index = 0; index < groupList.length(); index++) {

					JSONObject group = groupList.getJSONObject(index);

					noteID = noteService.createNote(staffID, responseQuestionID, noteTitle, noteDescription);
					int groupID = group.getInt("groupID");

					if(fileDetected) {

						noteAttachmentID = noteAttachmentService.createNoteAttachment(noteAttachmentURL, noteID);
						
						if(isDuplicate){
							
							isDuplicate = true;
							
							noteAttachmentURL = noteAttachmentService.updateNoteAttachmentName(noteAttachmentID, noteAttachmentURL);

							File storeFile = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));

							fileUpload.write(storeFile);
							responseObject.put("File-Result", "SUCCESS");
							responseObject.put("File-Message", "There is file uploaded.");
				
						} else {
							noteAttachmentService.updateNoteAttachmentNameShare(noteAttachmentID, noteAttachmentURL);
						}

					} else {
						responseObject.put("File-Result", "FAILED");
						responseObject.put("File-Ressage", "There is no file uploaded.");
					}

					PublishNoteResult publishNoteResult = noteService.publishNote(noteID, groupID, staffID);
					responseObject.put("result", publishNoteResult.toString());
					responseObject.put("message", publishNoteResult.getDefaultMessage());

				}

			} 

		} catch (Exception e) {
			e.printStackTrace();
		}

		out.write(responseObject.toString());

	}
}
