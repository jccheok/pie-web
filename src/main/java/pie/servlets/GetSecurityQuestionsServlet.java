package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.SecurityQuestion;
import pie.services.SecurityQuestionService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetSecurityQuestionsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 9191713033703746046L;
	
	SecurityQuestionService securityQuestionService;

	@Inject
	public GetSecurityQuestionsServlet(SecurityQuestionService securityQuestionService) {
		this.securityQuestionService = securityQuestionService;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		JSONArray securityQuestions = new JSONArray();
		
		for(SecurityQuestion securityQuestion : securityQuestionService.getAllSecurityQuestion()){
			
			JSONObject question = new JSONObject();
			question.put("securityQuestionID", securityQuestion.getSecurityQuestionID());
			question.put("securityQuestionDescription", securityQuestion.getSecurityQuestionDescription());
			
			securityQuestions.put(question);
		}

		responseObject.put("securityQuestions", securityQuestions);
		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
		
	}

}
