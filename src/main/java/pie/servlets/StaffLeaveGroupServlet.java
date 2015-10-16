package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.LeaveGroupResult;
import pie.services.GroupService;
import pie.services.StaffGroupService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StaffLeaveGroupServlet extends HttpServlet{

	private static final long serialVersionUID = 1350818991273599535L;

	StaffService staffService;
	GroupService groupService;
	StaffGroupService staffGroupService;

	@Inject
	public StaffLeaveGroupServlet(StaffService staffService, GroupService groupService, StaffGroupService staffGroupService) {
		this.staffService = staffService;
		this.groupService = groupService;
		this.staffGroupService = staffGroupService;
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

		LeaveGroupResult leaveGroupResult = staffGroupService.leaveGroup(groupID, staffID);
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", leaveGroupResult.toString());
		responseObject.put("message", leaveGroupResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}
}
