package pie.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

public class AuthService {

	private static final Key secretKey = MacProvider.generateKey();
	private static final String issuer = System.getenv("OPENSHIFT_APP_NAME");

	public static Key getSecretKey() {
		return secretKey;
	}

	public static String createToken(String subject, long ttlMillis, HashMap<String, Object> claims) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer) .signWith(signatureAlgorithm, secretKey);

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

	public static Claims parseJWT(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();

		return claims;
	}

}
