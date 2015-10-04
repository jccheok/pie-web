package pie.filters;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pie.StaffRole;
import pie.UserType;
import pie.services.AuthService;
import pie.services.StaffService;
import pie.utilities.Utilities;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthFilter implements Filter {

	StaffService staffService;
	AuthService authService;

	private String UNAUTHORIZED_MESSAGE = "UNAUTHORIZED: YOU ARE NOT AUTHORIZED TO ACCESS THIS RESOURCE";

	@Inject
	public AuthFilter(StaffService staffService, AuthService authService) {
		this.staffService = staffService;
		this.authService = authService;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;

		try {

			String token = getToken(servletRequest);

			try {
				Map<String, Object> decoded = authService.parseJWT(token);

				int userID = ((Integer) decoded.get("userID")).intValue();
				UserType userType = UserType.getUserType(((Integer) decoded.get("userTypeID")).intValue());
				String requestURI = (servletRequest).getRequestURL().toString();

				String accessType = requestURI.split("/")[3].toUpperCase();
				
				if (userType == UserType.ADMIN || userType.toString().equals(accessType)) {

					if (userType == UserType.STAFF && requestURI.split("/")[4].equals("group")) {

						String memberType = requestURI.split("/")[5];
						int groupID = 0;

						try {
							Map<String, String> requestParameters = Utilities.getParameters(servletRequest, "groupID");
							groupID = Integer.parseInt(requestParameters.get("groupID"));
						} catch (Exception e) {

							servletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
							return;
						}

						if (staffService.isMember(userID, groupID)) {
							StaffRole staffRole = staffService.getStaffRole(userID, groupID);

							if (memberType.equals("owner") && !staffRole.staffRoleIsOwner()) {
								servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
								return;
							} else if (memberType.equals("admin") && (!staffRole.staffRoleIsAdmin() || !staffRole.staffRoleIsOwner())) {
								servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
								return;
							}
						} else {
							servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
							return;
						}
					}

					filterChain.doFilter(servletRequest, servletResponse);

				} else {

					servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
					return;
				}
			} catch (Exception e) {
				throw new ServletException("UNAUTHORIZED: UNRECOGNIZED TOKEN", e);
			}

		} catch (Exception e) {
			servletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return;
		}
	}

	private String getToken(HttpServletRequest servletRequest) throws ServletException {

		final String token = servletRequest.getHeader("X-Auth-Token");

		if (token == null || token.equals("")) {
			throw new ServletException("UNAUTHORIZED: NO TOKEN FOUND IN HEADER");
		}

		return token;
	}
}
