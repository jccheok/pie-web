package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.constants.AddChildResult;
import pie.services.ParentService;

@Singleton
public class AddChildServlet {

	ParentService parentService;

	@Inject
	public AddChildServlet(ParentService parentService) {
		this.parentService = parentService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		int parentID = Integer.parseInt(request.getParameter("parentID"));
		int relationshipID = Integer.parseInt(request.getParameter("relationshipID"));
		String studentCode = request.getParameter("studentCode");
				
		AddChildResult addChildResult = parentService.addChild(parentID, relationshipID, studentCode);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", addChildResult.toString());
		responseObject.put("message", addChildResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
}
