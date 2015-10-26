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
import org.jsoup.Jsoup;

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

	UserHomeworkService userHomeworkService;
	GroupHomeworkService groupHomeworkService;
	UserService userService;
	ParentStudentService parentStudentService;

	@Inject
	public GetAllUserHomeworkServlet(UserHomeworkService userHomeworkService, GroupHomeworkService groupHomeworkService,
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

		UserHomework[] userHomework = userHomeworkService.getAllUserHomework(userID);

		JSONObject responseObject = new JSONObject();
		JSONArray homeworkList = new JSONArray();

		if (userHomework != null) {

			for (UserHomework homework : userHomework) {

				Staff staff = userHomeworkService.getUserHomeworkPublisher(homework.getUserHomeworkID());
				JSONObject homeworkObject = new JSONObject();
				String homeworkDescription = Jsoup.parse(homework.getHomework().getHomeworkDescription()).text();
				if (homeworkDescription.length() > 15) {
					homeworkDescription = homeworkDescription.substring(0, 15).concat("...");
				}

				homeworkObject.put("homeworkID", homework.getHomework().getHomeworkID());
				homeworkObject.put("userHomeworkID", homework.getUserHomeworkID());
				homeworkObject.put("homeworkTitle", homework.getHomework().getHomeworkTitle());
				homeworkObject.put("homeworkDescription", homeworkDescription);
				homeworkObject.put("publisherName", staff.getUserFullName());
				GroupHomework groupHomework = userHomeworkService.getGroupHomework(homework.getUserHomeworkID(),
						homework.getHomework().getHomeworkID());
				homeworkObject.put("publishedDate", Utilities.parseServletDateFormat(groupHomework.getPublishDate()));
				homeworkObject.put("homeworkIsGraded", groupHomework.isGraded());
				homeworkObject.put("homeworkIsAcknowledged", homework.isAcknowledged());
				homeworkObject.put("homeworkIsRead", homework.isRead());
				homeworkObject.put("groupID", groupHomework.getGroup().getGroupID());
				homeworkObject.put("groupName", groupHomework.getGroup().getGroupName());
				
				if(userService.getUser(userID).getUserType() == UserType.PARENT){

					homeworkObject.put("isAcknowledged", homework.isAcknowledged());

					Student[] children = parentStudentService.getChildren(userID);

					JSONArray childrenHomework = new JSONArray();
					for (Student child : children) {
						UserHomework childHomework = userHomeworkService
								.getChildHomework(homework.getHomework().getHomeworkID(), child.getUserID());

						if (childHomework != null) {
							JSONObject childHomeworkObject = new JSONObject();

							childHomeworkObject.put("childName", child.getUserFullName());
							childHomeworkObject.put("childID", child.getUserID());
							childHomeworkObject.put("childHomeworkID", childHomework.getUserHomeworkID());

							if (!childHomework.isSubmitted()) {
								childHomeworkObject.put("status", "Not submitted");
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
					homeworkList.put(homeworkObject);

				} else {

					homeworkObject.put("homeworkGrade", homework.getGrade());
					homeworkObject.put("homeworkIsMarked", homework.isMarked());
					homeworkObject.put("homeworkIsSubmitted", homework.isSubmitted());

				}
				homeworkList.put(homeworkObject);
			}
		}

		responseObject.put("sentHomework", homeworkList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
