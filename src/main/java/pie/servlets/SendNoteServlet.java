package pie.servlets;

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
import org.json.JSONObject;

import pie.constants.PublishNoteResult;
import pie.services.NoteAttachmentService;
import pie.services.NoteService;

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
		int groupID = 0;
		int responseQuestionID = 0;
		int noteAttachmentID = 1;
		boolean fileDetected = false;
		String noteTitle = null;
		String noteDescription = null;
		String noteAttachmentURL = null;
		FileItem fileUpload = null;

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();

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

						fileDetected = true;
						fileUpload = item;

						noteAttachmentURL = new File(item.getName()).getName();

					} else {

						if(item.getFieldName().equalsIgnoreCase("staffID")) {
							staffID = Integer.parseInt(item.getString());
							responseObject.put("staffID", staffID);
						} else if(item.getFieldName().equalsIgnoreCase("groupID")) {
							groupID = Integer.parseInt(item.getString());
							responseObject.put("groupID", groupID);
						} else if(item.getFieldName().equalsIgnoreCase("responseQuestionID")) {
							responseQuestionID = Integer.parseInt(item.getString());
							responseObject.put("responseQuestion", responseQuestionID);
						} else if(item.getFieldName().equalsIgnoreCase("noteTitle")) {
							noteTitle = item.getString();
							responseObject.put("noteTitle", noteTitle);
						} else if(item.getFieldName().equalsIgnoreCase("noteDescription")) {
							noteDescription = item.getString();
							responseObject.put("noteDescription", noteDescription);
						}
					} 
				}

			} else {
				responseObject.put("uploadResult", "No item found");
			}

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		if(staffID != 0 && responseQuestionID != 0) {

			noteID = noteService.createNote(staffID, responseQuestionID, noteTitle, noteDescription);

			if(fileDetected) {

				noteAttachmentID = noteAttachmentService.createNoteAttachment(noteAttachmentURL, noteID);
				noteAttachmentURL = noteAttachmentService.updateNoteAttachmentName(noteAttachmentID, noteAttachmentURL);

				File storeFile = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
				
				try {
					fileUpload.write(storeFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				responseObject.put("fileResult", "SUCCESS");
				responseObject.put("noteAttachmentID", noteAttachmentID);
				responseObject.put("noteAttachmentURL", noteAttachmentURL);

			} else {
				responseObject.put("fileResult", "FAILED - NO FILE UPLOADED");
			}

			PublishNoteResult publishNoteResult = noteService.publishNote(noteID, groupID, staffID);
			responseObject.put("result", publishNoteResult.toString());
			responseObject.put("message", publishNoteResult.getDefaultMessage());

		} else {
			responseObject.put("noteResult", "Creation of note failed");
		}

		out.write(responseObject.toString());

	}
}
