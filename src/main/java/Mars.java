import javax.swing.ImageIcon;
/**
 * CS2212
 * 6_TheWeather
 * 
 * Mars.java
 * This class represents the weather object for Mars and contains
 * all methods contained within the ADO class
 * 
 * @author team6 
 */
public class Mars extends ADO {
	/**
	 * Instance variables
	 */
	private double temperature;
	private double displayTemp;
	private double minTemp;
	private double displayMinTemp;
	private double maxTemp;
	private double displayMaxTemp;
	/**
	 * Default constructor for Mars
	 */
	public Mars()
	{
		super();
	}
    /**
     *
     * @param air the air pressure on mars
     * @param wind the wind speed on mars
     * @param temp the temperature on mars
     * @param humid the expected humidity on mars
     * @param windDir the wind direction on mars
     * @param sky the state of the sky on mars
     * @param state an image representing the sky state
     */
    public Mars(int air, double wind, double temp, int humid, String windDir, String sky, ImageIcon state) {
        super(air, wind, humid, windDir, sky, state);
        temperature = convertTemp(temp, this.getPreferences());
    }

    /**
     * @overrides the default mars constructor
     */
    public Mars(int air, double wind, double temp, double min, double max, int humid, String windDir, String sky, ImageIcon state) {
        super(air, wind,humid, windDir, sky, state);
        temperature = temp;
        displayTemp = convertTemp(temperature, this.getPreferences());
        minTemp = min;
        displayMinTemp = convertTemp(min,this.getPreferences());
        maxTemp = max;
        displayMaxTemp = convertTemp(max,this.getPreferences());
    }
    /**
     * Returns value of temperature of the object
     *
     * @return The temperature of the data object
     */
    public double getTemperature() {
        return displayTemp;
    }
   /**
     * Sets the current temperature based off of a change in units from user preferences
     */
    public void setTempUnits()
    {
    	displayTemp = convertTemp(temperature, this.getPreferences());
    }
    /**
     * This method sets the current temperature for the abstract data object
     *
     * @param temp the temperature value to be set
     */
    public void setTemperature(double temp) {
    	temperature = temp;
        displayTemp = convertTemp(temp, this.getPreferences());
    }

    /**
     * Returns the minimum temperature of data object
     *
     * @return The minimum temperature for the abstract data object
     */
    public double getMinTemp() {
        return displayMinTemp;
    }

    /**
     * This method sets the minimum temperature for the abstract data object
     *
     * @param min the minimum temperature value to be set
     */
    public void setMinTemp(double min) {
    	minTemp = min;
        displayMinTemp = convertTemp(min, this.getPreferences());
    }
     /**
     * Sets the current minimum temperature based off of a change in units from user preferences
     */
    public void setMinTempUnits()
    {
    	displayMinTemp = convertTemp(minTemp,this.getPreferences());
    }
    /**
     * Returns max temperature of data object
     *
     * @return The max temperature of the abstract data object
     */
    public double getMaxTemp() {
        return displayMaxTemp;
    }

    /**
     * This method sets the maximum temperature value for the abstract data
     * object
     *
     * @param max the maximum temperature value to be set
     */
    public void setMaxTemp(double max) {
    	maxTemp = max;
        displayMaxTemp = convertTemp(max, this.getPreferences());
    }
     /**
     * Sets the current maximum temperature based off of a change in units from user preferences
     */
    public void setMaxTempUnits()
    {
    	displayMaxTemp = convertTemp(maxTemp,this.getPreferences());
    }
    
/**
 * 
 * @param temp the temperature to be converted
 * @param preferences The user preferences specifying what units to convert to
 * @return the converted temperature
 */
    private double convertTemp(double temp,String preferences)
    {
    	if (preferences.equals("S"))
    	{
    		temp = temp + 273.15;
    	}
    	else if (preferences.equals("I"))
    	{
    		temp = (((temp)*9)/5) + 32;
    	}
        temp = Math.round(temp * 100) / 100D;
    	return temp;
    }
}
