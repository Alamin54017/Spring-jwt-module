package com.dipti.JWT_Test.Config;

import java.io.IOException;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dipti.JWT_Test.Utils.JwtUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

	public JwtRequestFilter(JwtUtils jwtUtils) {
		super();
		this.jwtUtils = jwtUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt=parseJwt(request);
			
			if(jwt!=null && jwtUtils.validateJwtToken(jwt)){
				String username=jwtUtils.extractUsername(jwt);
				List<SimpleGrantedAuthority> grantedAuthorities= 
						jwtUtils.extractAuthorities(jwt)
						.stream()
						.map(SimpleGrantedAuthority::new)
						.toList();

				UserDetails userDetails=new User(username,"",grantedAuthorities);

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
						new UsernamePasswordAuthenticationToken(userDetails,null, grantedAuthorities);

				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		} catch (Exception e) {
			logger.error("JWT Authentication Failed");
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth =request.getHeader(AUTHORIZATION);
		if(StringUtils.isNotEmpty(headerAuth)&&headerAuth.startsWith(BEARER_PREFIX)) {
			return headerAuth.substring(7);
		}
		return null;
	}
    
    
}
