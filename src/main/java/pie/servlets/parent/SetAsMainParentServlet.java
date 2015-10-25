package pie.servlets.parent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.SetAsMainParentResult;
import pie.services.ParentStudentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SetAsMainParentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8368684348044983899L;
	
	ParentStudentService parentStudentService;
	
	@Inject
	SetAsMainParentServlet(ParentStudentService parentStudentService) {
		this.parentStudentService = parentStudentService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int parentID = 0;
		int studentID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "parentID", "studentID");
			parentID = Integer.parseInt(requestParameters.get("parentID"));
			studentID = Integer.parseInt(requestParameters.get("studentID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		SetAsMainParentResult setAsMainParentResult = parentStudentService.setAsMainParent(parentID, studentID);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", setAsMainParentResult.toString());
		responseObject.put("message", setAsMainParentResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
