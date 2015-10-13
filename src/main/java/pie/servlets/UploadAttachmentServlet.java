package pie.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import pie.services.AttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadAttachmentServlet extends HttpServlet {

	private static final long serialVersionUID = 4834340741739873069L;
	int BUFFER_LENGTH = 4096;

	AttachmentService attachmentService;

	@Inject
	public UploadAttachmentServlet(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ROUGH DRAFT


		boolean isUploaded = false;
		int log = 0;
		
		PrintWriter out = response.getWriter();
		for (Part part : request.getParts()) {
			log = 1;
			InputStream is = request.getPart(part.getName()).getInputStream();
			log = 2;
			String fileName = attachmentService.getFileName(part);
			log = 3;
			FileOutputStream os = new FileOutputStream(System.getenv("OPENSHIFT_DATA_DIR") + fileName);
			log = 4;
			byte[] bytes = new byte[BUFFER_LENGTH];
			log = 5;
			int read = 0;
			log = 6;
			while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
				os.write(bytes, 0, read);
			}
			log = 7;
			os.flush();
			is.close();
			os.close();
			log = 8;
			out.println(fileName + " was uploaded to " + System.getenv("OPENSHIFT_DATA_DIR"));
			isUploaded = true;
		}

		if(isUploaded) {
			out.println("Success");
		} else {
			out.println("Failed" + log);
		}
		
	}
}
