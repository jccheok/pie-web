package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RegisterGroupServlet extends HttpServlet {

	private static final long serialVersionUID = -3878265942477329631L;

	private GroupService groupService;
	private StaffService staffService;

	@Inject
	public RegisterGroupServlet(GroupService groupService, StaffService staffService) {
		this.groupService = groupService;
		this.staffService = staffService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String groupName = null;
		String groupDescription = null;
		int groupMaxDailyHomeworkMinutes = 0;
		int groupTypeID = 0;
		String groupCode = null;
		int groupOwnerID = 0;
		Date expiryDate = null;
		int subjectID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupName", "groupDescription",
					"groupMaxDailyHomeworkMinutes", "groupTypeID", "groupCode", "staffID", "expiryDate", "subjectID");
			groupName = requestParameters.get("groupName");
			groupDescription = requestParameters.get("groupDescription");
			groupMaxDailyHomeworkMinutes = Integer.parseInt(requestParameters.get("groupMaxDailyHomeworkMinutes"));
			groupTypeID = Integer.parseInt(requestParameters.get("groupTypeID"));
			groupCode = requestParameters.get("groupCode");
			groupOwnerID = Integer.parseInt(requestParameters.get("staffID"));
			expiryDate = dateFormat.parse(requestParameters.get("expiryDate"));
			subjectID = Integer.parseInt(requestParameters.get("subjectID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		GroupType groupType = GroupType.getGroupType(groupTypeID);

		Staff groupOwner = staffService.getStaff(groupOwnerID);

		GroupRegistrationResult registrationResult = groupService.registerGroup(groupOwner, groupName,
				groupDescription, groupMaxDailyHomeworkMinutes, groupType, groupCode, expiryDate, subjectID);

		JSONObject responseObject = new JSONObject();
		responseObject.put("result", registrationResult.toString());
		responseObject.put("message", registrationResult.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());

	}

}
