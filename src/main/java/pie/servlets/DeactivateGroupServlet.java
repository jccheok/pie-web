package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.DeactivateGroupResult;
import pie.services.GroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class DeactivateGroupServlet {

	private static final long serialVersionUID = -1810174238170625377L;
	
	GroupService groupService;
	
	@Inject
	public DeactivateGroupServlet(GroupService groupService){
		this.groupService = groupService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		int groupID = 0;
		String userPassword = null;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID", "userPassword");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			userPassword = requestParameters.get("userPassword");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		DeactivateGroupResult deactivateGroupResult = groupService.deactivateGroup(groupID, staffID, userPassword);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", deactivateGroupResult.toString());
		responseObject.put("message", "Successfully enlisted all students!");

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
