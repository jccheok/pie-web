package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.Group;
import pie.services.GroupService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ViewGroupDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = -288687819678490074L;
	
	GroupService groupService;

	@Inject
	public ViewGroupDetailsServlet(GroupService groupService) {
		this.groupService = groupService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupID");
			groupID = Integer.parseInt(requestParameters.get("groupID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Group group = groupService.getGroup(groupID);

		JSONObject responseObject = new JSONObject();

		responseObject.put("groupID", group.getGroupID());
		responseObject.put("groupName", group.getGroupName());
		responseObject.put("schoolName", group.getSchool().getSchoolName());
		responseObject.put("groupDescription", group.getGroupDescription());
		responseObject.put("groupMaxDailyHomeworkMinutes", group.getGroupMaxDailyHomeworkMinutes());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
