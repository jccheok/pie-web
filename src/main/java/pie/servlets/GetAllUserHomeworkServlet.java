package pie.servlets;

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

import pie.Staff;
import pie.UserHomework;
import pie.services.GroupHomeworkService;
import pie.services.UserHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllUserHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -4832565000771009073L;
	
	UserHomeworkService userHomeworkService;
	GroupHomeworkService groupHomeworkService;

	@Inject
	public GetAllUserHomeworkServlet(UserHomeworkService userHomeworkService, GroupHomeworkService groupHomeworkService) {
		this.userHomeworkService = userHomeworkService;
		this.groupHomeworkService = groupHomeworkService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int userID = 0;
		String publishedDate = null;

		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "userID");
			userID = Integer.parseInt(requestParameters.get("userID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		UserHomework[] userHomework = userHomeworkService.getAllUserHomework(userID);

		JSONObject responseObject = new JSONObject();
		
		if (userHomework != null) {

			JSONArray homeworkList = new JSONArray();

			for (UserHomework homework : userHomework) {
				
				Staff staff = userHomeworkService.getUserHomeworkPublisher(homework.getUserHomeworkID());
				JSONObject homeworkObject = new JSONObject();
				publishedDate = dateFormat.format(userHomeworkService.getGroupHomework(homework.getUserHomeworkID(), staff.getUserID()).getPublishDate());
				homeworkObject.put("homeworkTitle", homework.getHomework().getHomeworkTitle());
				homeworkObject.put("homeworkDescription", homework.getHomework().getHomeworkDescription());
				homeworkObject.put("publisherName", staff.getUserFullName());
				homeworkObject.put("publishedDate", publishedDate);

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
