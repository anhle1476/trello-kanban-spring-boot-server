package com.codegym.kanban.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codegym.kanban.model.AppUser;
import com.codegym.kanban.model.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JwtVerifierFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;

	public JwtVerifierFilter(JwtConfig jwtConfig, SecretKey secretKey) {
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.replace(jwtConfig.getTokenPrefix(), "");

		try {
			tryAuthenticateJwtToken(token, request);
		}catch (SignatureException ex){
			throw new IllegalStateException("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
        	throw new IllegalStateException("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
        	throw new IllegalStateException("Expired JWT token");
        }catch (UnsupportedJwtException ex){
        	throw new IllegalStateException("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
        	throw new IllegalStateException("JWT claims string is empty");
        } catch (JwtException e) {
			throw new IllegalStateException("Token can not be verified");
		}

		filterChain.doFilter(request, response);
	}

	private void tryAuthenticateJwtToken(String token, HttpServletRequest request) {
		Claims claims = parseTokenClaims(token);

		UsernamePasswordAuthenticationToken authToken = buildAuthToken(claims);
		authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}
	
	private Claims parseTokenClaims(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
		return claimsJws.getBody();
	}

	private UsernamePasswordAuthenticationToken buildAuthToken(Claims claims) {
		Long id = ((Number) claims.get("userId")).longValue();
		String fullname = (String) claims.get("fullname");
		String email = (String) claims.get("email");
		Role role = Role.valueOf((String) claims.get("role"));
		
		AppUser principal = new AppUser();
		principal.setId(id);
		principal.setEmail(email);
		principal.setFullname(fullname);
		principal.setRole(role);
		
		List<SimpleGrantedAuthority> authorities= Arrays.asList(new SimpleGrantedAuthority(role.name()));

		return new UsernamePasswordAuthenticationToken(principal, null, authorities);
	}
}
