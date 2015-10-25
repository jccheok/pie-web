package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.DeactivateGroupResult;
import pie.services.GroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeactivateGroupServlet extends HttpServlet {
	
	private static final long serialVersionUID = -3475049810131574669L;
	
	GroupService groupService;
	
	@Inject
	public DeactivateGroupServlet(GroupService groupService){
		this.groupService = groupService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int staffID = 0;
		int groupID = 0;
		String authToken = null;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "staffID", "groupID", "authToken");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			authToken = requestParameters.get("authToken");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		DeactivateGroupResult deactivateGroupResult = groupService.deactivateGroup(groupID, staffID, authToken);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", deactivateGroupResult.toString());
		responseObject.put("message", deactivateGroupResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
