package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;

import pie.constants.JoinGroupResult;
import pie.services.GroupService;

public class UpdateGroupServlet {

	GroupService groupService;

	@Inject
	public UpdateGroupServlet(GroupService groupService) {
		this.groupService = groupService;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		int groupID = Integer.parseInt(request.getParameter("groupID"));
		String groupName = request.getParameter("groupName");
		String groupDescription = request.getParameter("groupDescription");
		int groupMaxDailyHomeworkMinutes = Integer.parseInt(request.getParameter("groupMaxDailyHomeworkMinutes"));

		boolean updateResult = groupService.updateGroup(groupID, groupName, groupDescription, groupMaxDailyHomeworkMinutes);

		JSONObject responseObject = new JSONObject();
		if(updateResult){
			responseObject.put("result", "Success");
			responseObject.put("message", "Group updated successfully");
		}else{
			responseObject.put("result", "Failed");
			responseObject.put("message", "Try again later");
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
	
	
}
