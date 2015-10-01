package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;

import pie.services.GroupService;
import pie.utilities.Utilities;

public class UpdateGroupServlet {

	GroupService groupService;

	@Inject
	public UpdateGroupServlet(GroupService groupService) {
		this.groupService = groupService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupID = 0;
		String groupName = null;
		String groupDescription = null;
		int groupMaxDailyHomeworkMinutes = 0;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "groupName",
					"groupDescription", "groupMaxDailyHomeworkMinutes");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			groupName = requestParameters.get("groupName");
			groupDescription = requestParameters.get("groupDescription");
			groupMaxDailyHomeworkMinutes = Integer.parseInt(requestParameters.get("groupMaxDailyHomeworkMinutes"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		boolean updateResult = groupService.updateGroup(groupID, groupName, groupDescription,
				groupMaxDailyHomeworkMinutes);

		JSONObject responseObject = new JSONObject();
		if (updateResult) {
			responseObject.put("result", "Success");
			responseObject.put("message", "Group updated successfully");
		} else {
			responseObject.put("result", "Failed");
			responseObject.put("message", "Try again later");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
