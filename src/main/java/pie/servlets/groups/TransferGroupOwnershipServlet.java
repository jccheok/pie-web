package pie.servlets.groups;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.User;
import pie.constants.TransferGroupOwnershipResult;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TransferGroupOwnershipServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8345217294139531580L;
	
	GroupService groupService;
	StaffGroupService staffGroupService;
	UserService userService;
	@Inject
	public TransferGroupOwnershipServlet(GroupService groupService, StaffGroupService staffGroupService, UserService userService){
		this.groupService = groupService;
		this.staffGroupService = staffGroupService;
		this.userService = userService;
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
		
		User owner = userService.getUser(ownerID);
		JSONObject responseObject = new JSONObject();
		if(owner.getUserEmail() != transfereeEmail){
			TransferGroupOwnershipResult transferGroupOwnershipResult = staffGroupService.transferGroupOwnership(ownerID, groupID, transfereeEmail, ownerPassword);
			responseObject.put("result", transferGroupOwnershipResult.toString());
			responseObject.put("message", transferGroupOwnershipResult.getDefaultMessage());
		}else{
			responseObject.put("result", "Same Email address");
			responseObject.put("message", "Do not use the same email address.");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
