/**
 * CS2212 
 * 6_TheWeather
 * 
 * NoConnectionException.java
 * This class handles the exception thrown when the application fails to connect to the API.
 * 
 * @author team6
 */
public class NoConnectionException extends Exception {

 	public NoConnectionException(String arg0) {
		super("Could not contact server. Please re-enter location.");
	}
}
