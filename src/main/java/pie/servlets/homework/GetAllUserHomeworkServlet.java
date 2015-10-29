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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.GroupHomework;
import pie.Staff;
import pie.Student;
import pie.UserHomework;
import pie.UserType;
import pie.services.GroupHomeworkService;
import pie.services.ParentStudentService;
import pie.services.UserHomeworkService;
import pie.services.UserService;
import pie.utilities.Utilities;

@Singleton
public class GetAllUserHomeworkServlet extends HttpServlet {

	private static final long serialVersionUID = 962612459025279323L;

	private UserHomeworkService userHomeworkService;
	private GroupHomeworkService groupHomeworkService;
	private UserService userService;
	private ParentStudentService parentStudentService;

	@Inject
	public GetAllUserHomeworkServlet(UserHomeworkService userHomeworkService,
			GroupHomeworkService groupHomeworkService,
			UserService userService, ParentStudentService parentStudentService) {
		this.userHomeworkService = userHomeworkService;
		this.groupHomeworkService = groupHomeworkService;
		this.userService = userService;
		this.parentStudentService = parentStudentService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userID = 0;
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userID");
			userID = Integer.parseInt(requestParameters.get("userID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserHomework[] allUserHomework = userHomeworkService.getAllUserHomework(userID);

		JSONObject responseObject = new JSONObject();
		JSONArray homeworkList = new JSONArray();
		
		if (allUserHomework != null) {

			for (UserHomework userHomework : allUserHomework) {
				JSONObject homeworkObject = new JSONObject();
				String homeworkDescription = Utilities.parseHtml(userHomework.getHomework().getHomeworkDescription());
				
				GroupHomework groupHomework = groupHomeworkService.getGroupHomework(userHomework.getGroupHomeworkID());
				Staff staff = groupHomework.getPublisher();
				
				homeworkObject.put("userHomeworkID", userHomework.getUserHomeworkID());
				homeworkObject.put("homeworkTitle", userHomework.getHomework().getHomeworkTitle());
				homeworkObject.put("homeworkDescription", homeworkDescription);
				homeworkObject.put("publisherName", staff.getUserFullName());	

				homeworkObject.put("publishedDate", Utilities.parseServletDateFormat(groupHomework.getPublishDate()));
				homeworkObject.put("homeworkIsGraded", groupHomework.isGraded());
				homeworkObject.put("homeworkIsRead", userHomework.isRead());
				homeworkObject.put("homeworkIsArchived", userHomework.isArchived());
				homeworkObject.put("groupID", groupHomework.getGroup().getGroupID());
				homeworkObject.put("groupName", groupHomework.getGroup().getGroupName());

				if (userService.getUser(userID).getUserType() == UserType.PARENT) {

					homeworkObject.put("isAcknowledged", userHomework.isAcknowledged());

					Student[] children = parentStudentService.getChildren(userID);

					JSONArray childrenHomework = new JSONArray();
					for (Student child : children) {
						UserHomework childHomework = userHomeworkService.getChildHomework(userHomework.getHomework()
								.getHomeworkID(), child.getUserID());

						if (childHomework.getGroupHomeworkID() == groupHomework.getGroupHomeworkID()) {
							JSONObject childHomeworkObject = new JSONObject();

							childHomeworkObject.put("childName", child.getUserFullName());
							childHomeworkObject.put("childID", child.getUserID());
							childHomeworkObject.put("childUserHomeworkID", childHomework.getUserHomeworkID());

							if (!childHomework.isSubmitted()) {
								childHomeworkObject.put("status", "Not Submitted");
							} else if (!childHomework.isMarked()) {
								childHomeworkObject.put("status", "Submitted");
							} else if (!childHomework.getGrade().equals("-")) {
								childHomeworkObject.put("status", "Marked");
							} else {
								childHomeworkObject.put("status", "Graded");
							}

							childrenHomework.put(childHomeworkObject);
						}
					}
					homeworkObject.put("childrenHomework", childrenHomework);

				} else {

					homeworkObject.put("homeworkGrade", userHomework.getGrade());
					homeworkObject.put("homeworkIsMarked", userHomework.isMarked());
					homeworkObject.put("homeworkIsSubmitted", userHomework.isSubmitted());

				}

				homeworkList.put(homeworkObject);

			}
		}

		responseObject.put("allHomework", homeworkList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
