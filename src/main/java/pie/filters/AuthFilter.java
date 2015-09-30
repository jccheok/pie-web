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

import pie.services.AuthService;

import com.google.inject.Singleton;

@Singleton
public class AuthFilter implements Filter {
	
	private AuthService authService;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		authService = new AuthService();
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		try {
			
			String token = getToken((HttpServletRequest) servletRequest);
			
			try {
	            Map<String, Object> decoded = authService.parseJWT(token);
	            
	            filterChain.doFilter(servletRequest, servletResponse);
	        } catch (Exception e) {
	            throw new ServletException("UNAUTHORIZED: UNRECOGNIZED TOKEN", e);
	        }
			
		} catch (Exception e) {
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

	private String getToken(HttpServletRequest servletRequest) throws ServletException {
		
		final String token = servletRequest.getHeader("X-Auth-Token");
		
		if (token == null || token.equals("")) {
			throw new ServletException("UNAUTHORIZED: NO TOKEN HEADER FOUND IN HEADER");
		}

		return token;
	}
}
