/**
 * CS2212 
 * 6_TheWeather
 * 
 * ShortTerm.java
 * This class contains all parameters and methods required for a shortTerm
 * forecast. It consists of an eight element array of Hourly objects.
 * 
 * @author team6
 */
public class ShortTerm implements java.io.Serializable {

    /**
     * Instance variables
     */
    private Hourly[] shortTerm;

    
    /**
     * Default constructor for long-term forecast
     */
    ShortTerm() {
        super();
    }
        
    /**
     * Constructor for the short term forecast
     *
     * @param hours the array representing the array of hourly objects for the
     * short term forecast
     */
    public ShortTerm(Hourly[] hours) {
        shortTerm = hours;
    }

    /**
     * Setter method for the short term forecast
     *
     * @param hours the array representing the array of hourly objects for the
     * short term forecast
     */
    public void setShortTerm(Hourly[] hours) {
        shortTerm = hours;
    }

    /**
     * Returns an array of Hourly objects.
     *
     * @return the short term forecast object
     */
    public Hourly[] getShortTerm() {
        return shortTerm;
    }

    /**
     * Returns the Hourly object stored in the ith index of the short term array
     *
     * @return the ith element in the shortTerm array
     * @param i index of shortTerm array; method returns object at array index i
     */
    public Hourly getHourly(int i) {
       return shortTerm[i];
    }
    
    /**
     * This method sets an hourly object at a specficed index in the short term array
     * @param hour the hour to be set
     * @param i the index at which the hour will be set
     */
    public void setHourly(Hourly hour, int i) {
    	shortTerm[i] = hour;
    }
}
