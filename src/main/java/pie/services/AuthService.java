package pie.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import com.google.inject.Singleton;

@Singleton
public class AuthService {

	private static final Key SECRET_KEY = MacProvider.generateKey();
	private static final String ISSUER = System.getenv("OPENSHIFT_APP_NAME");

	public Key getSecretKey() {
		return SECRET_KEY;
	}

	public String createToken(String subject, long ttlMillis, HashMap<String, Object> claims) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(ISSUER) .signWith(signatureAlgorithm, SECRET_KEY);

		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		if (claims != null) {
			builder.setClaims(claims);
		}

		return (builder.compact());
	}

	public Claims parseJWT(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();

		return claims;
	}

}
