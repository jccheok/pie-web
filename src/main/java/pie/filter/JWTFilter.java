package pie.filter;

import java.io.IOException;
import java.security.Key;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import com.auth0.jwt.JWTVerifier;

import io.jsonwebtoken.impl.crypto.MacProvider;
import pie.service.AuthService;

@WebFilter(filterName= "jwt-filter", urlPatterns = { "/secured/*" })
public class JWTFilter implements Filter {
	private JWTVerifier jwtVerifier;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        jwtVerifier = new JWTVerifier(
          DatatypeConverter.parseBase64Binary(AuthService.getSecretKey()),
          System.getenv("aud"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getToken((HttpServletRequest) request);

        try {
            Map<String, Object> decoded = jwtVerifier.verify(token);
            // Do something with decoded information like UserId
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("Unauthorized: Token validation failed", e);
        }
    }

    private String getToken(HttpServletRequest httpRequest) throws ServletException {
    	String token = null;
        final String authorizationHeader = httpRequest.getHeader("X-auth");
        if (authorizationHeader == null) {
            throw new ServletException("Unauthorized: No Authorization header was found");
        }

        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2) {
            throw new ServletException("Unauthorized: Format is Authorization: Bearer [token]");
        }

        String scheme = parts[0];
        String credentials = parts[1];

        Pattern pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
        if (pattern.matcher(scheme).matches()) {
            token = credentials;
        }
        return token;
    }

	@Override
	public void destroy() {
		
	}

}