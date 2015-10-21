package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import pie.Homework;
import pie.constants.DeleteHomeworkResult;
import pie.services.HomeworkService;
import pie.utilities.Utilities;

@Singleton
public class DeleteHomeworkServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4984801775289538426L;
	private HomeworkService homeworkService;

	@Inject
	public DeleteHomeworkServlet(HomeworkService homeworkService) {
		this.homeworkService = homeworkService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int homeworkID = 0;

		try {

			Map<String, String> requestParams = Utilities.getParameters(request, "homeworkID");
			homeworkID = Integer.parseInt(requestParams.get("homeworkID"));

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		Homework homework = homeworkService.getHomework(homeworkID);

		DeleteHomeworkResult result = homeworkService.deleteHomework(homework);
		JSONObject responseObject = new JSONObject();
		responseObject.put("result", result.toString());
		responseObject.put("message", result.getDefaultMessage());

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
