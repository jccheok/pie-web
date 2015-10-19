package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.UserHomework;
import pie.services.ParentStudentService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetHomeworkRecipientsServlet extends HttpServlet {

	private static final long serialVersionUID = -2356290397130180495L;
	
	UserHomeworkService userHomeworkService;
	ParentStudentService parentStudentService;
	
	@Inject
	public GetHomeworkRecipientsServlet(UserHomeworkService userHomeworkService, ParentStudentService parentStudentService) {
		this.userHomeworkService = userHomeworkService;
		this.parentStudentService = parentStudentService;
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
				
				JSONObject homeworkObject = new JSONObject();
				homeworkObject.put("userID", homework.getUser().getUserID());
				homeworkObject.put("userName", homework.getUser().getUserFullName());
				homeworkObject.put("userHomeworkID", homework.getUserHomeworkID());
				homeworkObject.put("hasSubmitted", homework.isSubmitted());
				homeworkObject.put("grade", homework.getGrade());
				homeworkObject.put("hasMarked", homework.isMarked());
				
				homeworkObject.put("parent", parentStudentService.getMainParent(homework.getUser().getUserID()).getUserFullName());
				
				homeworkList.put(homeworkObject);
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
