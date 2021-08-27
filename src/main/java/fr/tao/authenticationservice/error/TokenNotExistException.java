package fr.tao.authenticationservice.error;

public class TokenNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenNotExistException(String message) {
		super(message);
	}
	
	

}
