import javax.swing.ImageIcon;

/**
 * CS2212 
 * 6_TheWeather
 * 
 * Daily.java
 * This class contains all variables necessary for a full day forecast, as well
 * as all get and set methods. The long term forecast will be an array of Daily
 * objects. In addition to all variables from the super class, it stores a
 * string of the day the object corresponds to.
 * 
 * @author team6
 */
public class Daily extends ADO implements java.io.Serializable {

    /**
     * Instance variables
     */
    private String day;

    /**
     * Constructor for the daily weather object
     *
     * @param days the current day that the object with representing
     * @param air the air pressure for the daily weather object
     * @param wind the wind speed for the daily weather object
     * @param temp the current temperature for the we daily weather object
     * @param min the minimum temperature for the daily weather object
     * @param max the maximum temperature for the daily weather object
     * @param humid the humidity for the daily weather object
     * @param windDir the wind direction for the daily weather object
     * @param sky the sky condition for daily the weather object
     * @param state the image that goes along with the associated sky condition
     */
    public Daily(String days, double temp, double min, double max, String sky, ImageIcon state) {
        super(temp, min, max, sky, state);
        day = days;
    }
        //new Daily(weekdays[time.get(GregorianCalendar.DAY_OF_WEEK)],   temp,    temp_min,    temp_max,    skyState,    icon);

    /**
     * Returns the name of the corresponding day.
     *
     * @return the day that the object is representing
     */
    public String getDay() {
        return day;
    }

    /**
     * This method sets the day that the object is representing
     *
     * @param days the day to be set for the day object
     */
    public void setDay(String days) {
        day = days;
    }
}
