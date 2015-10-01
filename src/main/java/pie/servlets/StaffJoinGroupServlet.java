package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.StaffRole;
import pie.constants.JoinGroupResult;
import pie.services.StaffRoleService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StaffJoinGroupServlet extends HttpServlet {

	private static final long serialVersionUID = 5666243950594572419L;

	StaffService staffService;
	StaffRoleService staffRoleService;

	@Inject
	public StaffJoinGroupServlet(StaffService staffService, StaffRoleService staffRoleService) {
		this.staffService = staffService;
		this.staffRoleService = staffRoleService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupID = 0;
		int staffID = 0;
		int staffRoleID = 0;
		String groupCode = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "staffID",
					"staffRoleID", "groupCode");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			staffID = Integer.parseInt(requestParameters.get("staffID"));
			staffRoleID = Integer.parseInt(requestParameters.get("staffRoleID"));
			groupCode = requestParameters.get("groupCode");

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		StaffRole staffRole = staffRoleService.getStaffRole(staffRoleID);

		JoinGroupResult joinGroupResult = staffService.joinGroup(groupID, staffID, groupCode, staffRole);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
