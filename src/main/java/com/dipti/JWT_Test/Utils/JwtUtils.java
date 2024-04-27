package com.dipti.JWT_Test.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

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
	
	@Value("${jwt.secret}")
	private String SECRET_KEY="secret";
	
	@Value("${jwt.jwtExp}")
	private int JWT_EXP=300000;
	
	public String generateToken(UserDetails userdetails) {
		List<String> authorities=userdetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
		Map<String, Object> claims=new HashMap<>();
		claims.put("Authorities", authorities);
		
		
		return Jwts.builder()
				.claims(claims)
				.subject(userdetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+JWT_EXP))
				.signWith(getSignInKey(),Jwts.SIG.HS256)
				.compact();
	}
	
	private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	
	public boolean valiadteJwtToken(String token) {
		try {
			Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
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
		Claims claims=Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
		
		return claims.getSubject();
	}
	
	
	
	public List<String> extractAuthorities(String token){
		Claims claims=Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
		
		return claims.get("Authorities",List.class);
	}
}
