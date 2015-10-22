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

import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Homework;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class GetHomeworkDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 8818437379452661718L;

	private HomeworkService homeworkService;

	@Inject
	public GetHomeworkDetailsServlet(HomeworkService homeworkService) {
		this.homeworkService = homeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int homeworkID = 0;

		try {

			Map<String, String> requestParams = Utilities.getParameters(request, "homeworkID");
			homeworkID = Integer.parseInt(requestParams.get("homeworkID"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		Homework homework = homeworkService.getHomework(homeworkID);

		JSONObject responseObject = new JSONObject();

		responseObject.put("dateCreated", dateFormat.format(homework.getHomeworkDateCreated()));
		responseObject.put("authorBy", homework.getHomeworkAuthor().getUserFullName());
		responseObject.put("description", homework.getHomeworkDescription());
		responseObject.put("level", homework.getHomeworkLevel());
		responseObject.put("title", homework.getHomeworkTitle());
		responseObject.put("subject", homework.getHomeworkSubject());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
