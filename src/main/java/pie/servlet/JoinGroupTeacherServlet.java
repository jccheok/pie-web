package pie.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pie.service.TeacherService;
import pie.service.TeacherService.JoinGroupResult;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class JoinGroupTeacherServlet extends HttpServlet {

	TeacherService teacherService;

	@Inject
	public JoinGroupTeacherServlet(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		int groupID = Integer.parseInt(request.getParameter("groupID"));
		int teacherID = Integer.parseInt(request.getParameter("teacherID"));
		String teacherRoleName = request.getParameter("teacherRoleName");

		JoinGroupResult joinGroupResult = teacherService.joinGroup(groupID,
				teacherID, teacherRoleName);

		JSONObject responseObject = new JSONObject();

		for (JoinGroupResult result : JoinGroupResult.values()) {
			if (result == joinGroupResult) {
				responseObject.put("result", result.toString());
				responseObject.put("message", result.getDefaultMessage());
				break;
			}
		}

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}

}
