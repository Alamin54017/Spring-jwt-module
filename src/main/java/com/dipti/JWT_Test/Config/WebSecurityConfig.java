package com.dipti.JWT_Test.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.dipti.JWT_Test.Service.CustomUserDetailsService;

@Configuration
public class WebSecurityConfig {

	
	 private final CustomUserDetailsService customUserDetailsService;
	 private final JwtRequestFilter jwtRequestFilter;
	
	 
	 public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
		super();
		this.customUserDetailsService = customUserDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
	}
	
	 
	 @Bean
	 @SuppressWarnings("deprecation")
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	 
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	 
	 @Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		 httpSecurity
		 			.csrf(AbstractHttpConfigurer::disable)
		 			.headers(headerConfigurer-> headerConfigurer
		 					.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
		 					)
		 			.authorizeHttpRequests(requestConfigurer -> requestConfigurer
	                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
	                        .requestMatchers(antMatcher("/authenticate/**")).permitAll()
	                        .requestMatchers(antMatcher("/user/register/**")).permitAll()
	                        .requestMatchers(antMatcher("/user/**")).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
	                        .requestMatchers(antMatcher("/admin/**")).hasAuthority("ROLE_ADMIN")
	                        .anyRequest().permitAll()
		 					)
		 			.userDetailsService(customUserDetailsService)
		 			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
		 			.sessionManagement(sessionConfigurer-> sessionConfigurer
		 					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		 					);
		 
		 return httpSecurity.build();
	 }
	 
}
