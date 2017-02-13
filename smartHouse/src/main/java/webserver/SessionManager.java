package webserver;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.*;
import utils.Constants;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

/**
 * Created by loulou on 25/01/2017.
 */
public class SessionManager {

	public static Boolean checkAuthentication(String token) {
		try {
			parseJWT(token);
		} catch (JwtException e) {
			System.out.println("erreur liée à la vérification du JWT : " + e.getMessage());
			return false;
		}
		return true;
	}

	public static IdToken getGoogleIdToken(String idTokenString) {
		GoogleIdToken idToken = null;
		try {
			HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			JsonFactory jsonFactory = new JacksonFactory();

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(Constants.CLIENT_ID))
					.build();

			try {
				idToken = verifier.verify(idTokenString);
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return idToken;
	}

	public static String getUserId(IdToken idToken) {
		String userId = null;
		if (idToken != null) {
			IdToken.Payload payload = idToken.getPayload();
			userId = payload.getSubject();
		} else {
			System.out.println("Invalid ID token.");
		}
		return userId;
	}

	public static String createJWT(String issuer, String subject, long ttlMillis) {

		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("projetnsoc");
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder()
				.setIssuedAt(now)
				.setSubject(subject)
				.setIssuer(issuer)
				.signWith(signatureAlgorithm, signingKey);

		//if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	private static void parseJWT(String jwt) throws JwtException {

		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary("projetnsoc"))
				.parseClaimsJws(jwt).getBody();
	}

	public static Timestamp getExpirationTime(String jwt) throws JwtException {
		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary("projetnsoc"))
				.parseClaimsJws(jwt).getBody();

		return new Timestamp(claims.getExpiration().getTime());
	}
}

