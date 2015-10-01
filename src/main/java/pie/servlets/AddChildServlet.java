package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.AddChildResult;
import pie.services.ParentService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AddChildServlet extends HttpServlet{

	private static final long serialVersionUID = 811697721299744514L;
	
	ParentService parentService;

	@Inject
	public AddChildServlet(ParentService parentService) {
		this.parentService = parentService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int parentID = 0;
		int relationshipID = 0;
		String studentCode = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "parentID", "relationshipID",
					"studentCode");
			parentID = Integer.parseInt(requestParameters.get("parentID"));
			relationshipID = Integer.parseInt(requestParameters.get("relationshipID"));
			studentCode = requestParameters.get("studentCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		AddChildResult addChildResult = parentService.addChild(parentID, relationshipID, studentCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", addChildResult.toString());
		responseObject.put("message", addChildResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
