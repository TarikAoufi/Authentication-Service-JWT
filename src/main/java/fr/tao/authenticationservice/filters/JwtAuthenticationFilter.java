package fr.tao.authenticationservice.filters;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tao.authenticationservice.utilities.JWTUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());

	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LOGGER.info("attemptAuthentication");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
			FilterChain chain, Authentication authResult) throws IOException, ServletException {			
		LOGGER.info("successfulAuthentication");
		User user = (User) authResult.getPrincipal();
		Algorithm algo1 = Algorithm.HMAC256(JWTUtil.SECRET);
		
		String jwtAccessToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString()) // nom de l'application qui a fournie le token
				.withClaim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
				.sign(algo1); // signature
		
		String jwtRefreshToken = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME))
				.withIssuer(request.getRequestURL().toString()) 
				.sign(algo1);

		Map<String, String> idToken = new HashMap<>();
		idToken.put(JWTUtil.ACCESS_TOKEN_NAME, jwtAccessToken);
		idToken.put(JWTUtil.REFRESH_TOKEN_NAME, jwtRefreshToken);
		// Réponse contenant des données json
		response.setContentType("application/json");
		new ObjectMapper().writeValue(response.getOutputStream(), idToken);
	}

}
