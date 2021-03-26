package com.codegym.kanban.security.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codegym.kanban.dto.LoginSuccessResponseDTO;
import com.codegym.kanban.model.AppUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	  private final AuthenticationManager authenticationManager;
	  private final JwtConfig jwtConfig;
	  private final SecretKey secretKey;

	  public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
	    this.authenticationManager = authenticationManager;
	    this.jwtConfig = jwtConfig;
	    this.secretKey = secretKey;
	  }

	  @Override
	  public Authentication attemptAuthentication(
			  HttpServletRequest request, 
			  HttpServletResponse response) throws AuthenticationException {
	    try {
	      UsernamePasswordAuthenticationRequest authRequest = mapAuthenticationRequest(request);

	      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	        authRequest.getEmail(),
	        authRequest.getPassword());

	      return authenticationManager.authenticate(authToken);
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }

	private UsernamePasswordAuthenticationRequest mapAuthenticationRequest(HttpServletRequest request)
			throws IOException, JsonParseException, JsonMappingException {
		return new ObjectMapper()
	        .readValue(request.getInputStream(), UsernamePasswordAuthenticationRequest.class);
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String token = buildJwtToken(authResult);
		LoginSuccessResponseDTO responseDTO = new LoginSuccessResponseDTO(jwtConfig.getTokenPrefix() + token);
		String responseBody = new ObjectMapper().writeValueAsString(responseDTO);
		
		response.setContentType("application/json");
		response.setStatus(200);
		response.getWriter().write(responseBody);
	}

	private String buildJwtToken(Authentication authResult) {
		AppUser principal = (AppUser) authResult.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", principal.getId());
		claims.put("email", principal.getEmail());
		claims.put("role", principal.getRole().name());
		claims.put("fullname", principal.getFullname());
		
		return Jwts.builder()
				.setSubject(authResult.getName())
				.setClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
				.signWith(secretKey)
				.compact();
	}
}