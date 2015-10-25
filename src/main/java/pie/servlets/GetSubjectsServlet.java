package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Subject;
import pie.services.SubjectService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetSubjectsServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3504261937026508606L;
	
	SubjectService subjectService;

	@Inject
	public GetSubjectsServlet(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject responseObject = new JSONObject();
		JSONArray subjectList = new JSONArray();

		for (Subject subject : subjectService.getAllSubjects()) {
			JSONObject subjectObject = new JSONObject();
			subjectObject.put("subjectID", subject.getSubjectID());
			subjectObject.put("subjectName", subject.getSubjectName());
			subjectObject.put("subjectAbbreviation", subject.getAbbreviation());
			
			subjectList.put(subjectObject);
		}

		responseObject.put("subjects", subjectList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
