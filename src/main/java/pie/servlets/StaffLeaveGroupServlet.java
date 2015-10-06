package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.LeaveGroupResult;
import pie.services.GroupService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;

public class StaffLeaveGroupServlet {
	private static final long serialVersionUID = 5666243950594572419L;

	StaffService staffService;
	GroupService groupService;

	@Inject
	public StaffLeaveGroupServlet(StaffService staffService, GroupService groupService) {
		this.staffService = staffService;
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupID = 0;
		int staffID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "staffID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			staffID = Integer.parseInt(requestParameters.get("staffID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		LeaveGroupResult leaveGroupResult = staffService.leaveGroup(groupID, staffID);
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", leaveGroupResult.toString());
		responseObject.put("message", leaveGroupResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
