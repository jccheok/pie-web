package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.constants.GenericResult;
import pie.services.GroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UpdateGroupServlet extends HttpServlet{

	private static final long serialVersionUID = 6026995148320527089L;
	
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
		boolean groupIsOpen = false;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID", "groupName",
					"groupDescription", "groupMaxDailyHomeworkMinutes", "groupIsOpen");
			groupID = Integer.parseInt(requestParameters.get("groupID"));
			groupName = requestParameters.get("groupName");
			groupDescription = requestParameters.get("groupDescription");
			groupMaxDailyHomeworkMinutes = Integer.parseInt(requestParameters.get("groupMaxDailyHomeworkMinutes"));
			groupIsOpen = Integer.parseInt(requestParameters.get("groupIsOpen")) == 1;

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		boolean updateResult = groupService.updateGroup(groupID, groupName, groupDescription,
				groupMaxDailyHomeworkMinutes, groupIsOpen);

		JSONObject responseObject = new JSONObject();
		if (updateResult) {
			responseObject.put("result", GenericResult.SUCCESS.toString());
			responseObject.put("message", "Group updated successfully.");
		} else {
			responseObject.put("result", GenericResult.FAILED.toString());
			responseObject.put("message", "Please try again later.");
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
