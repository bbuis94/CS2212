import javax.swing.ImageIcon;

/**
 * CS2212 
 * 6_TheWeather
 * 
 * Hourly.java
 * This class represents the object for weather in 3 hour
 * intervals
 * 
 * @author team6
 */
public class Hourly extends ADO implements java.io.Serializable {

    /**
     * Instance variables
     */
    private int hour;

    /**
     * Constructor for the hourly weather object
     *
     * @para time the current hour for the weather object
     * @param air the air pressure for the weather object
     * @param wind the wind speed for the weather object
     * @param temp the current temperature for the weather object
     * @param min the minimum temperature for the weather object
     * @param max the maximum temperature for the weather object
     * @param humid the humidity for the weather object
     * @param windDir the wind direction for the weather object
     * @param sky the sky condition fo the weather object
     * @param state the image that goes along with the associated sky condition
     */
    public Hourly(int time, int air, double wind, double temp, double min, double max, int humid, String windDir, String sky, ImageIcon state) {
        super(air, wind, temp, min, max, humid, windDir, sky, state);
        hour = time;
    }

    /**
     * Returns the time of the first hour of the object's three forecast
     *
     * @return the initial hour that the object is representing
     */
    public int getHour() {
        return hour;
    }

    /**
     * This method sets the initial hour that the weather object is representing
     *
     * @param hourly the hour to be set
     */
    public void setHour(int hourly) {
        hour = hourly;
    }
}
