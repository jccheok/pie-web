package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Parent;
import pie.User;
import pie.UserHomework;
import pie.UserType;
import pie.services.ParentStudentService;
import pie.services.UserHomeworkService;
import pie.services.UserService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetHomeworkRecipientsServlet extends HttpServlet {

	private static final long serialVersionUID = -2356290397130180495L;
	
	UserHomeworkService userHomeworkService;
	ParentStudentService parentStudentService;
	UserService userService;
	
	@Inject
	public GetHomeworkRecipientsServlet(UserHomeworkService userHomeworkService, ParentStudentService parentStudentService, UserService userService) {
		this.userHomeworkService = userHomeworkService;
		this.parentStudentService = parentStudentService;
		this.userService = userService;
	}
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int groupHomeworkID = 0;
		int homeworkID = 0;
		int publisherID = 0;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "groupHomeworkID", "publisherID", "homeworkID");
			groupHomeworkID = Integer.parseInt(requestParameters.get("groupHomeworkID"));
			homeworkID = Integer.parseInt(requestParameters.get("homeworkID"));
			publisherID = Integer.parseInt(requestParameters.get("publisherID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserHomework[] userHomework = userHomeworkService.getUserHomeworkRecipients(homeworkID, groupHomeworkID, publisherID);

		JSONObject responseObject = new JSONObject();
		
		
		if (userHomework != null) {
			
			JSONArray homeworkList = new JSONArray();
			
			for(UserHomework homework : userHomework){

				User user = userService.getUser(homework.getUser().getUserID());
				if(user.getUserType() == UserType.STUDENT){
					
					JSONObject homeworkObject = new JSONObject();
					
					homeworkObject.put("userID", user.getUserID());
					homeworkObject.put("userName", user.getUserFullName());
					homeworkObject.put("userHomeworkID", homework.getUserHomeworkID());
					homeworkObject.put("hasSubmitted", homework.isSubmitted());
					homeworkObject.put("grade", homework.getGrade());
					homeworkObject.put("hasMarked", homework.isMarked());
					
					Parent parent = parentStudentService.getMainParent(homework.getUser().getUserID());
					homeworkObject.put("parent", parent.getUserFullName());
					
					homeworkList.put(homeworkObject);
				}
				
				
			}
		
			responseObject.put("publishedHomeworkDetails", homeworkList);
			
		} else {
			responseObject.put("result", "No users recieved");
			responseObject.put("message", "Homework was published to no one.");
		}
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
