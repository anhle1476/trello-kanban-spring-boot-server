package com.codegym.kanban.security;

import static com.codegym.kanban.model.Role.ROLE_ADMIN;
import static com.codegym.kanban.model.Role.ROLE_USER;

import java.util.Arrays;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codegym.kanban.security.jwt.JwtConfig;
import com.codegym.kanban.security.jwt.JwtUsernamePasswordAuthenticationFilter;
import com.codegym.kanban.security.jwt.JwtVerifierFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	  private final PasswordEncoder passwordEncoder;
	  private final UserDetailsService userDetailsService;
	  private final JwtConfig jwtConfig;
	  private final SecretKey secretKey;

	  public SecurityConfig(
	      PasswordEncoder passwordEncoder, 
	      UserDetailsService userDetailsService,
	      JwtConfig jwtConfig, 
	      SecretKey secretKey) {
	    this.passwordEncoder = passwordEncoder;
	    this.userDetailsService = userDetailsService;
	    this.jwtConfig = jwtConfig;
	    this.secretKey = secretKey;
	  }

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
	    http
	      .csrf().disable()
	      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	      .and()
	      .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
	      .addFilterAfter(new JwtVerifierFilter(jwtConfig, secretKey), JwtUsernamePasswordAuthenticationFilter.class)
	      // Basic
	      .authorizeRequests()
	      .antMatchers("/", "/login", "/register").permitAll()
	      .antMatchers("/admin/api/**").hasAnyAuthority(ROLE_ADMIN.name())
	      .antMatchers("/api/**").hasAnyAuthority(ROLE_ADMIN.name(), ROLE_USER.name())	      
	      .anyRequest()
	      .authenticated();
	    
	    http.cors();
	    
	  }

	  @Bean
	  public DaoAuthenticationProvider provider() {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setPasswordEncoder(passwordEncoder);
	    provider.setUserDetailsService(userDetailsService);
	    return provider;
	  }

	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.authenticationProvider(provider());
	  }
	  
	  @Bean
	  CorsConfigurationSource corsConfigurationSource() {
		  CorsConfiguration configuration = new CorsConfiguration();
		  configuration.setAllowedOrigins(Arrays.asList("*"));
		  configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		  configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		  configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		  source.registerCorsConfiguration("/**", configuration);
		  return source;
	  }
	  

}
