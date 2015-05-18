
import javax.swing.ImageIcon;
import java.util.GregorianCalendar;

/**
 * CS2212 
 * 6_TheWeather
 * 
 * Current.java
 * This class stores all information about the current weather. In addition to
 * all variables from the super class, it stores the date and time it was
 * created, as well as the time of sunrise and sunset of the day.
 * 
 * @author team6
 */
public class Current extends ADO implements java.io.Serializable {

    /**
     * Instance variables
     */
    private GregorianCalendar time;
    private GregorianCalendar sunrise;
    private GregorianCalendar sunset;

    /**
     * Default constructor for the current object Creates an empty object with
     * default preferences SI
     */
    public Current() {
        super();
    }

    /**
     * Constructor for the current weather object
     *
     * @param now the current time for the current weather object
     * @param air the air pressure for the current weather object
     * @param wind the wind speed for the current weather object
     * @param temp the current temperature for the current weather object
     * @param min the minimum temperature for the current weather object
     * @param max the maximum temperature for the current weather object
     * @param humid the humidity for the current weather object
     * @param windDir the wind direction for the current weather object
     * @param sky the sky condition for the current weather object
     * @param rise the projected sunrise time for the current weather object
     * @param set the projected sunset time for the current weather object
     * @param state the image that goes along with the associated sky condition
     */
    public Current(GregorianCalendar timing, GregorianCalendar rise, GregorianCalendar set, int air, double wind, double temp, double min, double max, int humid, String windDir, String sky, ImageIcon state) {
        super(air, wind, temp, min, max, humid, windDir, sky, state);
        time = timing;
        sunrise = rise;
        sunset = set;
    }

    /**
     * Returns the date and time the object was created.
     *
     * @return the hour of the current weather object
     */
    public GregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the current time for the current weather object
     *
     * @param timing the time to be set
     */
    public void setTime(GregorianCalendar timing) {
        time = timing;
    }

    /**
     * Returns the projected time of day the sun rises
     *
     * @return the projected time of the sunrise for the current weather object
     */
    public GregorianCalendar getSunRise() {
        return sunrise;
    }

    /**
     * Sets the projected sunrise time for the current weather object
     *
     * @param rise time of the sunrise to be set
     */
    public void setSunRise(GregorianCalendar rise) {
        sunrise = rise;
    }

    /**
     * Returns the projected time of sunset for the day
     *
     * @return the projected time of the sunset for the current weather object
     */
    public GregorianCalendar getSunSet() {
        return sunset;
    }

    /**
     * Sets the projected sunset time for the current weather object
     *
     * @param set time of the sunset to be set
     */
    public void setSunSet(GregorianCalendar set) {
        sunset = set;
    }
}
