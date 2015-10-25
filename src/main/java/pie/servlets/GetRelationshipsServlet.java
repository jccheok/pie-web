package pie.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import pie.Relationship;
import pie.services.RelationshipService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GetRelationshipsServlet extends HttpServlet {

	private static final long serialVersionUID = 1105261160099232417L;

	RelationshipService relationshipService;

	@Inject
	public GetRelationshipsServlet(RelationshipService relationshipService) {
		this.relationshipService = relationshipService;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		JSONObject responseObject = new JSONObject();
		
		JSONArray relationshipList = new JSONArray();
		for (Relationship relationship : relationshipService.getAllRelationships()) {
			
			JSONObject relationshipDetails = new JSONObject();
			relationshipDetails.put("relationshipID", relationship.getRelationshipID());
			relationshipDetails.put("relationshipName", relationship.getRelationshipName());
			relationshipList.put(relationshipDetails);
		}
		responseObject.put("relationshipList", relationshipList);

		PrintWriter out = response.getWriter();
		out.write(responseObject.toString());
	}
}