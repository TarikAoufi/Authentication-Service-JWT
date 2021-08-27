package fr.tao.authenticationservice.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tao.authenticationservice.entities.AppUser;
import fr.tao.authenticationservice.error.TokenNotExistException;
import fr.tao.authenticationservice.service.UserService;
import fr.tao.authenticationservice.utilities.JWTUtil;

@RestController
public class AccountRestController {
	
	public static final Logger LOGGER = Logger.getLogger(AccountRestController.class.getName());
	
	private UserService userService;

	public AccountRestController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping(path = JWTUtil.ENDPOINT_REFRESH_TOKEN)
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String header = request.getHeader(JWTUtil.AUTH_HEADER);
		if (header != null && header.startsWith(JWTUtil.PREFIX)) {
			try {
				String refreshToken = header.substring(JWTUtil.PREFIX.length());
				Algorithm algorithm = Algorithm.HMAC256(JWTUtil.SECRET);
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
				String username = decodedJWT.getSubject();

				AppUser appUser = userService.findUserByUsername(username);
				String jwtAccessToken = JWT.create().withSubject(appUser.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME))
						.withIssuer(request.getRequestURL().toString()) // nom de l'application qui a fournie le token
						.withClaim("roles",
								appUser.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toList()))
						.sign(algorithm);
				Map<String, String> idToken = new HashMap<>();
				idToken.put(JWTUtil.ACCESS_TOKEN_NAME, jwtAccessToken);
				idToken.put(JWTUtil.REFRESH_TOKEN_NAME, refreshToken);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
			} catch (Exception e) {
				LOGGER.log( Level.SEVERE, e.toString(), e );
				throw e;
			}
		}  else {
			throw new TokenNotExistException("The refresh token is required!");
		}			
	}

}
