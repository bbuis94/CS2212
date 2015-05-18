/**
 * CS2212
 * 6_TheWeather
 * 
 * BadLocationException.java
 * This class handles the exception thrown when an invalid location is entered
 * 
 * @author team6 
 */
public class BadLocationException extends Exception {
	public BadLocationException(String arg0) {
		super("Invalid! Please Enter a New Location: i.e. \"London, Ca\" ");
	}
}
