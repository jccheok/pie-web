package pie.servlets.homework;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.GroupHomework;
import pie.Staff;
import pie.Student;
import pie.UserHomework;
import pie.services.GroupHomeworkService;
import pie.services.ParentStudentService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllParentHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -4832565000771009073L;
	
	UserHomeworkService userHomeworkService;
	GroupHomeworkService groupHomeworkService;
	ParentStudentService parentStudentService;

	@Inject
	public GetAllParentHomeworkServlet(UserHomeworkService userHomeworkService, GroupHomeworkService groupHomeworkService, ParentStudentService parentStudentService) {
		this.userHomeworkService = userHomeworkService;
		this.groupHomeworkService = groupHomeworkService;
		this.parentStudentService = parentStudentService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int parentID = 0;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "parentID");
			parentID = Integer.parseInt(requestParameters.get("parentID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		UserHomework[] userHomework = userHomeworkService.getAllUserHomework(parentID);
		
		JSONObject responseObject = new JSONObject();
		
		if (userHomework != null) {

			JSONArray homeworkList = new JSONArray();

			for (UserHomework homework : userHomework) {
				
				Staff staff = userHomeworkService.getUserHomeworkPublisher(homework.getUserHomeworkID());
				JSONObject homeworkObject = new JSONObject();
				
				homeworkObject.put("homeworkID", homework.getHomework().getHomeworkID());
				homeworkObject.put("userHomeworkID", homework.getUserHomeworkID());
				homeworkObject.put("homeworkTitle", homework.getHomework().getHomeworkTitle());
				homeworkObject.put("homeworkDescription", homework.getHomework().getHomeworkDescription());
				homeworkObject.put("publisherName", staff.getUserFullName());
				
				GroupHomework groupHomework = userHomeworkService.getGroupHomework(homework.getUserHomeworkID(), homework.getHomework().getHomeworkID());
				homeworkObject.put("publishedDate", dateFormat.format(groupHomework.getPublishDate()));
				
				homeworkObject.put("isGraded", groupHomework.isGraded());
				homeworkObject.put("isAcknowledged", homework.isAcknowledged());
				
				Student[] children = parentStudentService.getChildren(parentID);
				
				JSONArray childrenHomework = new JSONArray();
				for(Student child : children){
					UserHomework childHomework = userHomeworkService.getChildHomework(homework.getHomework().getHomeworkID(), child.getUserID());
					
					if(childHomework != null){
						JSONObject childHomeworkObject = new JSONObject();
						
						childHomeworkObject.put("childName", child.getUserFullName());
						childHomeworkObject.put("childID", child.getUserID());
						childHomeworkObject.put("homeworkGrade", childHomework.getGrade());
						childHomeworkObject.put("homeworkSubmitted", childHomework.isSubmitted());
						childHomeworkObject.put("homeworkMarked", childHomework.isMarked());
						
						childrenHomework.put(childHomeworkObject);
						
					}
					
				}
				homeworkObject.put("childrenHomework", childrenHomework);

				homeworkList.put(homeworkObject);
			}

			responseObject.put("sentHomework", homeworkList);
		} else {
			responseObject.put("result", "No Sent Homework");
			responseObject.put("message", "No Homework was sent by this user");
		}
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
