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
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.constants.PublishHomeworkResult;
import pie.services.HomeworkAttachmentService;
import pie.services.HomeworkService;

@Singleton
public class PublishHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = -6931499866512426095L;
	private static final int maxRequestSize = 1024 * 1024 * 10;
	private static final int memorySize = 1024 * 1024 * 3;

	/*
	 * author: cheok jia chin test at: 22/10/2015 Modified: 23/10/2015
	 */

	HomeworkService homeworkService;
	HomeworkAttachmentService homeworkAttachmentService;

	@Inject
	public PublishHomeworkServlet(HomeworkService homeworkService,
			HomeworkAttachmentService homeworkAttachmentService) {
		this.homeworkService = homeworkService;
		this.homeworkAttachmentService = homeworkAttachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int homeworkID = -1;
		int staffID = 0;
		String homeworkTitle = null;
		String homeworkSubject = null;
		String homeworkDescription = null;
		int homeworkMinutesReqStudent = 0;
		String homeworkLevel = null;
		boolean fileDetected = false;
		// attachment
		String homeworkAttachmentURL = null;
		FileItem fileUpload = null;
		int homeworkAttachmentID = 1;

		JSONObject responseObject = new JSONObject();
		PrintWriter out = response.getWriter();

		if (homeworkAttachmentService.checkIfHomeworkFolderExist()) {
			responseObject.put("folderResult", "Homework Folder exist");
		} else {
			responseObject.put("folderResult", "Homework Folder did not exist but was created during the process");
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(memorySize);
		factory.setRepository(new File(System.getenv("OPENSHIFT_TMP_DIR")));

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxRequestSize);

		try {

			List<FileItem> items = upload.parseRequest(request);
			String[] fields = { "staffID", "homeworkTitle", "homeworkSubject", "homeworkDescription",
					"homeworkMinutesReqStudent", "homeworkLevel" };
			JSONArray notFoundParams = new JSONArray();

			for (FileItem x : items) {
				if (x.getString().length() == 0) {
					notFoundParams.put(x.getFieldName());
				}

			}

			if (notFoundParams.length() == 0 && !items.isEmpty()) {

				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (!item.isFormField() && item.getSize() > 0) {

						fileDetected = true;
						fileUpload = item;

						homeworkAttachmentURL = new File(item.getName()).getName();

					} else {

						if (item.getFieldName().equalsIgnoreCase("staffID")) {
							staffID = Integer.parseInt(item.getString());
							responseObject.put("staffID", staffID);
						} else if (item.getFieldName().equalsIgnoreCase("homeworkTitle")) {
							homeworkTitle = item.getString();
							responseObject.put("homeworkTitle", homeworkTitle);
						} else if (item.getFieldName().equalsIgnoreCase("homeworkSubject")) {
							homeworkSubject = item.getString();
							responseObject.put("homeworkSubject", homeworkSubject);
						} else if (item.getFieldName().equalsIgnoreCase("homeworkDescription")) {
							homeworkDescription = item.getString();
							responseObject.put("homeworkDescription", homeworkDescription);
						} else if (item.getFieldName().equalsIgnoreCase("homeworkMinutesReqStudent")) {
							homeworkMinutesReqStudent = Integer.parseInt(item.getString());
							responseObject.put("homeworkMinutesReqStudent", homeworkMinutesReqStudent);
						} else if (item.getFieldName().equalsIgnoreCase("homeworkLevel")) {
							homeworkLevel = item.getString();
							responseObject.put("homeworkLevel", homeworkLevel);
						}
					}
				}

				homeworkID = homeworkService.publishHomework(staffID, homeworkTitle, homeworkSubject,
						homeworkDescription, homeworkMinutesReqStudent, homeworkLevel);

				if (homeworkID != -1) {
					responseObject.put("result", PublishHomeworkResult.SUCCESS.toString());
					responseObject.put("message", PublishHomeworkResult.SUCCESS.getDefaultMessage());
				} else {
					responseObject.put("result", PublishHomeworkResult.FAILED_TO_UPDATE_HOMEWORK.toString());
					responseObject.put("message", PublishHomeworkResult.FAILED_TO_UPDATE_HOMEWORK.getDefaultMessage());
				}

				if (fileDetected && homeworkID != -1) {

					homeworkAttachmentID = homeworkAttachmentService.createHomeworkAttachment(homeworkAttachmentURL,
							homeworkID);
					homeworkAttachmentURL = homeworkAttachmentService.updateHomeworkAttachmentName(homeworkAttachmentID,
							homeworkAttachmentURL);

					File storeFile = new File(
							homeworkAttachmentService.getHomeworkAttachmentDIR(homeworkAttachmentURL));

					fileUpload.write(storeFile);

					responseObject.put("fileResult", "SUCCESS");
					responseObject.put("homeworkAttachmentID", homeworkAttachmentID);
					responseObject.put("homeworkAttachmentURL", homeworkAttachmentURL);

				} else {
					responseObject.put("fileResult", "NO FILE UPLOADED");
				}

			} else {
				responseObject.put("result", "No value found");
				responseObject.put("message", notFoundParams);
			}

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		out.write(responseObject.toString());
	}
}
