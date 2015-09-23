package pie.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

public class AuthService {

	private final static Key deezKey = MacProvider.generateKey();

	public static Key getDeezKey() {
		return deezKey;
	}

	public static String createToken(String id, String issuer, String subject, long ttlMillis,
			HashMap<String, Object> claims) {

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		JwtBuilder builder = Jwts.builder().setClaims(claims).setId(id).setIssuedAt(now).setSubject(subject)
				.setIssuer(issuer).signWith(signatureAlgorithm, deezKey);

		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		return (builder.compact());
	}

	public static Claims parseJWT(String jwt) {
		Claims claims = Jwts.parser().setSigningKey(AuthService.getDeezKey()).parseClaimsJws(jwt).getBody();

		return claims;
	}

}
