/**
 * CS2212 
 * 6_TheWeather
 * 
 * InternalServerError.java
 * This class handles the exception thrown when there is an internal server error.
 * 
 * @author team6
 */

public class InternalServerError extends Exception {
	public InternalServerError(String arg0) {
		super("Server under load: request could not be processed.");
	}
}
