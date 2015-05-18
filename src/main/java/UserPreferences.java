/**
 * CS2212 
 * 6_TheWeather
 * 
 * UserPreferences.java
 * UserPreferences represents the strings that describe the user's preferences
 * Help to update the display accordingly. 
 * 
 * @author team6
 */
public class UserPreferences {
    
    // Attributes
    private String temperatureUnit;
    private String speedUnit;
    private String pressureUnit;
    
    
// Default Constructor
    UserPreferences (){
        setSI();    // Default units are SI
    }    
    
    /*******************************
    FUNCTION DECLARATIONS BEGIN HERE
    *******************************/
    /*
    * setUserPreferences will configure display units based on a serialized object
    * @preferences represents the preference string saved
    * default units are assumed to be SI
    */
    public void setUserPreferences(String preferences)
    {
        char c;
        try
        {
        c = preferences.charAt(0); // Get the character stored inpreferences
        }
            catch (StringIndexOutOfBoundsException ex)  // If the string preferences is an empty string
        {
            c = 'S';    // Set default units to SI
        }
        
        switch (c)  // Switch to determine the serialized object
        {
            case 'm':
            case 'M':
                setMetric();        // Metric 
                break;
                
            case 'i':
            case 'I':
                setImperial();      // Imperial
                break;
            
            case 's':
            case 'S':
                setSI();            //System Internationale
                break;
                
            default:                // Default is Metric
                break;
        }
    }
    

    
    // Getter for temperature Units
    public String getTemperatureUnit()
    {
        return temperatureUnit;
    }
    
    // Getter for speed Units
    public String getSpeedUni()
    {
        return speedUnit;
    }
    
    // Getter for pressure Units
    public String getPressureUnit()
    {
        return pressureUnit;
    }
    
    
    // Private Helper Functions to Set units
    private void setMetric()
    {
        temperatureUnit = " C";
        speedUnit = " km/h";
        pressureUnit = " hPa";
    }
    
    private void setImperial()
    {
        temperatureUnit = " F";
        speedUnit = " mi/h";
        pressureUnit = " hPa";
    }
    
    private void setSI()
    {
        temperatureUnit = " K";
        speedUnit = " km/h";
        pressureUnit = " hPa";
    }

}
