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


		PrintWriter out = response.getWriter();
		for (Part part : request.getParts()) {
			InputStream is = request.getPart(part.getName()).getInputStream();
			String fileName = attachmentService.getFileName(part);
			FileOutputStream os = new FileOutputStream(System.getenv("OPENSHIFT_DATA_DIR") + fileName);
			byte[] bytes = new byte[BUFFER_LENGTH];
			int read = 0;
			while ((read = is.read(bytes, 0, BUFFER_LENGTH)) != -1) {
				os.write(bytes, 0, read);
			}
			os.flush();
			is.close();
			os.close();
			out.println(fileName + " was uploaded to " + System.getenv("OPENSHIFT_DATA_DIR"));

		}

	}
}
