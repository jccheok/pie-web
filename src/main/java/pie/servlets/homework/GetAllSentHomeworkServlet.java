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
import pie.services.GroupHomeworkService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetAllSentHomeworkServlet extends HttpServlet{

	private static final long serialVersionUID = -8358466550092241188L;
	
	GroupHomeworkService groupHomeworkService;
	
	@Inject
	public GetAllSentHomeworkServlet(GroupHomeworkService groupHomeworkService){
		this.groupHomeworkService = groupHomeworkService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int publisherID = 0;
		
		try {

			Map<String, String> requestParameters = Utilities.getParameters(request, "publisherID");
			publisherID = Integer.parseInt(requestParameters.get("publisherID"));

		} catch (Exception e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}
		
		GroupHomework[] groupHomework = groupHomeworkService.getAllSentHomework(publisherID);
		
		JSONObject responseObject = new JSONObject();
		if(groupHomework != null){
			
			JSONArray sentHomeworkList = new JSONArray();		

			for(GroupHomework homework : groupHomework){
				
				JSONObject homeworkObject = new JSONObject();
				homeworkObject.put("homeworkTitle", homework.getHomework().getHomeworkTitle());
				homeworkObject.put("homeworkDescription", homework.getHomework().getHomeworkDescription());
				homeworkObject.put("publisherName", homework.getPublisher().getUserFullName());
				homeworkObject.put("publishedDate", homework.getPublishDate());
				
				sentHomeworkList.put(homeworkObject);
			}
			
			responseObject.put("sentHomework", sentHomeworkList);
		}else{
			responseObject.put("result", "No Sent Homework");
			responseObject.put("message", "No Homework was sent by this user");
		}
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}
