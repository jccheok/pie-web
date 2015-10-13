package pie.servlets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import pie.services.AttachmentService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@MultipartConfig(location = "/var/lib/openshift/560246382d52714ebe00004d/app-root/data")
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

		Collection<Part> parts = request.getParts();

		out.write("<h2> Total parts : " + parts.size() + "</h2>");

		for (Part part : parts) {
			printEachPart(part, out);
			part.write(getFileName(part));
		}
	}

	private void printEachPart(Part part, PrintWriter pw) {
		StringBuffer sb = new StringBuffer();
		sb.append("<p>");
		sb.append("Name : " + part.getName());
		sb.append("<br>");
		sb.append("Content Type : " + part.getContentType());
		sb.append("<br>");
		sb.append("Size : " + part.getSize());
		sb.append("<br>");
		for (String header : part.getHeaderNames()) {
			sb.append(header + " : " + part.getHeader(header));
			sb.append("<br>");
		}
		sb.append("</p>");
		pw.write(sb.toString());

	}

	private String getFileName(Part part) {
		String partHeader = part.getHeader("content-disposition");
		for (String cd : partHeader.split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
