package com.dipti.JWT_Test.Utils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Component
public class JwtUtils {

	private static final Logger logger= LoggerFactory.getLogger(JwtUtils.class);
	

	private SecretKey SECRET_KEY;
	
	@Value("${jwt.jwtExp}")
	private int JWT_EXP;
	
	public String generateToken(UserDetails userdetails) {
		List<String> authorities=userdetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		Map<String, Object> claims=new HashMap<>();
		claims.put("Authorities", authorities);
		SECRET_KEY=getSignInKey();
		
		return Jwts.builder()
				.claims(claims)
				.subject(userdetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+JWT_EXP))
				.signWith(SECRET_KEY, Jwts.SIG.HS256)
				.compact();
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = new byte[32]; // 256 bits
		new SecureRandom().nextBytes(keyBytes);
		return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
	
	
	public boolean validateJwtToken(String token) {
		try {

			Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
			return true;
		}
		catch(SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			logger.error("Invalid Token");
		}
		catch(ExpiredJwtException ex){
			logger.error("Token expired");
		}
		return false;
	}
	
	
	public String extractUsername(String token) {
		Claims claims=Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
		
		return claims.getSubject();
	}
	
	
	
	public List<String> extractAuthorities(String token){
		Claims claims=Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
		
		return claims.get("Authorities",List.class);
	}
}
