package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.GroupType;
import pie.Staff;
import pie.constants.GroupRegistrationResult;
import pie.services.GroupService;
import pie.services.StaffService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterGroupServlet extends HttpServlet {

	private static final long serialVersionUID = -3878265942477329631L;
	
	private GroupService groupService;
	private StaffService teacherService;
	
	@Inject
	public RegisterGroupServlet(GroupService groupService, StaffService teacherService) {
		this.groupService = groupService;
		this.teacherService = teacherService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String groupName = request.getParameter("groupName");
		String groupDescription = request.getParameter("groupName");
		int groupMaxDailyHomeworkMinutes = Integer.valueOf(request.getParameter("groupMaxDailyHomeworkMinutes"));
		GroupType groupType = GroupType.getGroupType(Integer.valueOf(request.getParameter("groupTypeID")));
		String groupCode = request.getParameter("groupCode");
		int groupOwnerID = Integer.valueOf(request.getParameter("teacherID"));
		
		Staff groupOwner = teacherService.getStaff(groupOwnerID);
		
		GroupRegistrationResult registrationResult = groupService.registerGroup(groupOwner, groupName, groupDescription, groupMaxDailyHomeworkMinutes, groupType, groupCode);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
