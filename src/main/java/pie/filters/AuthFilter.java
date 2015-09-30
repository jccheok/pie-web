package pie.filters;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import pie.services.AuthService;

import com.auth0.jwt.JWTVerifier;
import com.google.inject.Singleton;

@Singleton
public class AuthFilter implements Filter {

	private JWTVerifier jwtVerifier;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		jwtVerifier = new JWTVerifier(DatatypeConverter.parseBase64Binary(AuthService.getSecretKey()), "link here");
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
	            Map<String, Object> decoded = jwtVerifier.verify(token);
	            
	            filterChain.doFilter(servletRequest, servletResponse);
	        } catch (Exception e) {
	            throw new ServletException("UNAUTHORIZED: UNRECOGNIZED TOKEN", e);
	        }
			
		} catch (Exception e) {
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}
	}

	private String getToken(HttpServletRequest servletRequest) throws ServletException {
		String token = null;
		
		final String authorizationHeader = servletRequest.getHeader("authorization");
		if (authorizationHeader == null) {
			throw new ServletException("UNAUTHORIZED: NO AUTHENTICATION HEADER FOUND");
		}

		String[] parts = authorizationHeader.split(" ");
		if (parts.length != 2) {
			throw new ServletException("UNAUTHORIZED: Format is Authorization: Bearer [token]");
		}

		String scheme = parts[0];
		String credentials = parts[1];

		Pattern pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
		if (pattern.matcher(scheme).matches()) {
			token = credentials;
		}
		return token;
	}
}
