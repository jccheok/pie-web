package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.StaffRole;
import pie.constants.JoinGroupResult;
import pie.services.StaffRoleService;
import pie.services.StaffService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class JoinGroupStaffServlet extends HttpServlet {

	private static final long serialVersionUID = 5666243950594572419L;

	StaffService staffService;
	StaffRoleService staffRoleService;

	@Inject
	public JoinGroupStaffServlet(StaffService staffService, StaffRoleService staffRoleService) {
		this.staffService = staffService;
		this.staffRoleService = staffRoleService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		int groupID = Integer.parseInt(request.getParameter("groupID"));
		int staffID = Integer.parseInt(request.getParameter("staffID"));
		int staffRoleID = Integer.parseInt(request.getParameter("staffRoleID"));
		StaffRole staffRole = staffRoleService.getStaffRole(staffRoleID);
		String groupCode = request.getParameter("groupCode");

		JoinGroupResult joinGroupResult = staffService.joinGroup(groupID, staffID, groupCode, staffRole);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", joinGroupResult.toString());
		responseObject.put("message", joinGroupResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
