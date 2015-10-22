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

import pie.services.NoteAttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadNoteServlet extends HttpServlet {

	private static final long serialVersionUID = 503101502986293125L;

	NoteAttachmentService noteAttachmentService;

	@Inject
	public UploadNoteServlet(NoteAttachmentService noteAttachmentService) {
		this.noteAttachmentService = noteAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		JSONObject responseObject = new JSONObject();

		int noteID = 1;
		int maxRequestSize = 1024*1024*10;
		int memorySize = 1024*1024*3;
		int noteAttachmentID = 0;
		String noteAttachmentURL = null;

		if(noteAttachmentService.checkIfNoteFolderExist()) {
			responseObject.put("Debug Log", "Note Folder exist");
		} else {
			responseObject.put("Debug Log", "Note Folder did not exist but was created during the process");
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(memorySize);
		factory.setRepository(new File(System.getenv("OPENSHIFT_TMP_DIR")));


		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxRequestSize);

		try {

			List<FileItem> items = upload.parseRequest(request);
			
			if(items != null && items.size() > 0) {
				responseObject.put("result", items + " is found");
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();
					responseObject.put("staff", item.getFieldName() + " | " + item.getName());
					if (!item.isFormField() && item.getSize() > 0) {
						responseObject.put("Name", item.getName());
						responseObject.put("Size", item.getSize());
						noteAttachmentURL = new File(item.getName()).getName();
						responseObject.put("AttachmentName", noteAttachmentURL);

						noteAttachmentID = noteAttachmentService.createNoteAttachment(noteAttachmentURL, noteID);
						responseObject.put("AttachmentID", noteAttachmentID);

						noteAttachmentURL = noteAttachmentService.updateNoteAttachmentName(noteAttachmentID, noteAttachmentURL);
						responseObject.put("UpdatedAttachmentName", noteAttachmentURL);

						File storeFile = new File(noteAttachmentService.getNoteAttachmentDIR(noteAttachmentURL));
						responseObject.put("FolderLocation", storeFile);

						item.write(storeFile);
					} else {
						responseObject.put("result", "FAILED");
						if(item.getFieldName() == "staffID") {
							int staffID = Integer.parseInt(item.getName());
							responseObject.put("staffID", staffID);
						} 
					}
				}
			} else {
				responseObject.put("result", "No file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.write(responseObject.toString());		
	}

}
