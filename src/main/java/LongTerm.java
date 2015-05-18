/**
 * CS2212 
 * 6_TheWeather
 * 
 * LongTerm.java
 * This class contains all parameters and methods required for a long term
 * forecast. It consists of an array of Daily objects.
 * 
 * @author team6
 */
public class LongTerm implements java.io.Serializable {

    /**
     * Instance variables
     */
    private Daily[] longTerm;

    
    
    /**
     * Default constructor for long-term forecast
     */
    LongTerm() {
        super();
    }
    
    /**
     * Constructor for the long term forecast
     *
     * @param days the array for the long term forecast
     */
    public LongTerm(Daily[] days) {
        longTerm = days;
    }

    /**
     * Setter method for the long term forecast
     *
     * @param days the array for the long term forecast
     */
    public void setLongTerm(Daily[] days) {
        longTerm = days;
    }

    /**
     * Returns the array of Daily objects.
     *
     * @return the short term forecast object
     */
    public Daily[] getLongTerm() {
        return longTerm;
    }

    /**
     * Returns the Daily object stored at the specified index of the longTerm
     * array
     *
     * @return the object representing the correct day based off an integer
     * input (number between 0 and 4 is entered
     * @param i index of longTerm array; method return object at index i
     */
    public Daily getDaily(int i) {
        return longTerm[i];
    }
    
    /**
     * This method sets a daily object at a specified location in the long term array
     * @param day the day to be set
     * @param i the index at which the day will be set
     */
    public void setDaily(Daily day, int i)
    {
    	longTerm[i] = day;
    }
}
