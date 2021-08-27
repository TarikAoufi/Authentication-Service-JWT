package fr.tao.authenticationservice.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import fr.tao.authenticationservice.utilities.JWTUtil;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	/**
	 * Permet de traiter toute requête reçu qui demande une ressource.
	 * Elle recupére le JWT présent dans le header afin de vérifer la signature 
	 * pour authoriser l'utilisateur ou pas.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getServletPath().equals(JWTUtil.ENDPOINT_REFRESH_TOKEN)) {
			filterChain.doFilter(request, response);
		}
		else {
			String header = request.getHeader(JWTUtil.AUTH_HEADER);
			
			if(header != null && header.startsWith(JWTUtil.PREFIX)) {
				try {
	
					String jwt = header.substring(JWTUtil.PREFIX.length());
					Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
					JWTVerifier jwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
					String username = decodedJWT.getSubject();
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					
					Collection<GrantedAuthority> authorities = new ArrayList<>();
					for (String role : roles) {
						authorities.add(new SimpleGrantedAuthority(role));
					}
					UsernamePasswordAuthenticationToken authenticationToken = 
							new UsernamePasswordAuthenticationToken(username, null, authorities);
					
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					response.setHeader("error-message", e.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
			} else {
				filterChain.doFilter(request, response);
			}
		}
		
	}

}
