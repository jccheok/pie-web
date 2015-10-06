package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.TransferGroupOwnershipResult;
import pie.services.GroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class TransferGroupOwnershipServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8345217294139531580L;
	
	GroupService groupService;
	
	@Inject
	public TransferGroupOwnershipServlet(GroupService groupService){
		this.groupService = groupService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int ownerID = 0;
		int groupID = 0;
		String ownerPassword = null;
		String transfereeEmail = null;
		
		try {
		
			Map<String, String> requestParameters = Utilities.getParameters(request, "ownerID", "groupID", "ownerPassword", "transfereeEmail");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			ownerID = Integer.parseInt(requestParameters.get("ownerID"));
			ownerPassword = requestParameters.get("ownerPassword");
			transfereeEmail = requestParameters.get("transfereeEmail");
			
		} catch (Exception e) {
			
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		TransferGroupOwnershipResult transferGroupOwnershipResult = groupService.transferGroupOwnership(ownerID, groupID, transfereeEmail, ownerPassword);
		
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", transferGroupOwnershipResult.toString());
		responseObject.put("message", transferGroupOwnershipResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
