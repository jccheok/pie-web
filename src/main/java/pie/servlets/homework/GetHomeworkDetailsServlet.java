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

import pie.GroupHomework;
import pie.Homework;
import pie.Staff;
import pie.Student;
import pie.User;
import pie.UserHomework;
import pie.UserType;
import pie.services.ParentStudentService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetHomeworkDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 8818437379452661718L;
	
	private UserHomeworkService userHomeworkService;
	private ParentStudentService parentStudentService;

	@Inject
	public GetHomeworkDetailsServlet(UserHomeworkService userHomeworkService, ParentStudentService parentStudentService) {
		this.userHomeworkService = userHomeworkService;
		this.parentStudentService = parentStudentService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userHomeworkID = -1;

		try {

			Map<String, String> requestParams = Utilities.getParameters(request, "userHomeworkID");
			userHomeworkID = Integer.parseInt(requestParams.get("userHomeworkID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserHomework userHomework = userHomeworkService.getUserHomework(userHomeworkID);
		User recipientUser = userHomework.getUser();
		Homework homework = userHomework.getHomework();
		Staff homeworkPublisher = userHomeworkService.getUserHomeworkPublisher(userHomeworkID);
		
		JSONObject responseObject = new JSONObject();
		
		responseObject.put("dateCreated", Utilities.parseServletDateFormat(homework.getHomeworkDateCreated()));
		responseObject.put("publisherName", homeworkPublisher.getUserFullName());
		
		responseObject.put("homeworkDescription", homework.getHomeworkDescription());
		
		responseObject.put("level", homework.getHomeworkLevel());
		responseObject.put("homeworkTitle", homework.getHomeworkTitle());
		responseObject.put("subject", homework.getHomeworkSubject());
		
		GroupHomework groupHomework = userHomeworkService.getGroupHomework(userHomeworkID, homework.getHomeworkID());
		responseObject.put("homeworkIsGraded", groupHomework.isGraded());
		responseObject.put("publishedDate", Utilities.parseServletDateFormat(groupHomework.getPublishDate()));
		responseObject.put("groupID", groupHomework.getGroup().getGroupID());
		responseObject.put("groupName", groupHomework.getGroup().getGroupName());
		
		if (recipientUser.getUserType() == UserType.PARENT) {

			responseObject.put("isAcknowledged", userHomework.isAcknowledged());

			Student[] children = parentStudentService.getChildren(recipientUser.getUserID());

			JSONArray childrenHomework = new JSONArray();
			for (Student child : children) {
				UserHomework childHomework = userHomeworkService.getChildHomework(homework.getHomeworkID(), child.getUserID());

				if (childHomework != null) {
					JSONObject childHomeworkObject = new JSONObject();

					childHomeworkObject.put("childName", child.getUserFullName());
					childHomeworkObject.put("childID", child.getUserID());
					childHomeworkObject.put("childUserHomeworkID", childHomework.getUserHomeworkID());
					childHomeworkObject.put("childHomeworkGrade", childHomework.getGrade());
					childHomeworkObject.put("childHomeworkIsMarked", childHomework.isMarked());
					childHomeworkObject.put("childHomeworkIsSubmitted", childHomework.isSubmitted());
					childrenHomework.put(childHomeworkObject);
				}
			}
			responseObject.put("childrenHomework", childrenHomework);

		} else if (recipientUser.getUserType() == UserType.STUDENT) {
			responseObject.put("homeworkGrade", userHomework.getGrade());
			responseObject.put("homeworkIsMarked", userHomework.isMarked());
			responseObject.put("homeworkIsSubmitted", userHomework.isSubmitted());
		}
		
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
