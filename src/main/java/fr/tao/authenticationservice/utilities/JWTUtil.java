package fr.tao.authenticationservice.utilities;

public class JWTUtil {
	
	public static final long ACCESS_TOKEN_EXPIRATION_TIME = 12*60*1000; // 12 min
	public static final long REFRESH_TOKEN_EXPIRATION_TIME = 15*60*1000; 
	public static final String AUTH_HEADER = "Authorization";
	public static final String SECRET = "ceciEstUnSecret";
	public static final String PREFIX = "Bearer ";
	public static final String ACCESS_TOKEN_NAME = "access-token";
	public static final String REFRESH_TOKEN_NAME = "refresh-token";
	/* endpoints */
	public static final String ENDPOINT_REFRESH_TOKEN = "/refreshToken";
}
