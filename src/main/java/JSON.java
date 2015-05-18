import java.io.*;
import java.net.*;
import javax.swing.ImageIcon;
import java.util.GregorianCalendar;
import org.apache.commons.io.*;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

/**
 * CS2212 
 * 6_TheWeather
 * 
 * JSON.java
 * This class is responsible for querying the OpenWeatherMap API and saving all required data.
 * 
 * @author team6
 */
public class JSON {
//	private String url = "http://api.openweathermap.org/data/2.5/forecast?q=london,ca&cnt=7";

    private final String HOST = "http://api.openweathermap.org"; //host and protocol 
    private final String PATH_CURRENT = "/data/2.5/weather";	//where we'll be getting the current weather from
    private final String PATH_FORECAST_SHORTTERM = "/data/2.5/forecast";	//where we'll be getting short term and long term forecasts from
    private final String PATH_FORECAST_LONGTERM = "/data/2.5/forecast/daily/";
    private final String PATH_ICON = "/img/w/";
    private final String OWM_MAIN = "main";
    private final String OWM_WEATHER = "weather";
    private final String OWM_WIND = "wind";
    private final String ARRAY_JSON = "list";
    private final String SUN_JSON = "sys";
    private final String TIME	= "dt";
    private final String RETURN_CODE = "cod";
    
    //Open Weather Map MAIN constants for short term, long term, and current
    private final String OWM_HUMIDITY = "humidity";
    private final String OWM_TEMP = "temp";
    private final String OWM_TEMP_MAX = "temp_max";
    private final String OWM_TEMP_MIN = "temp_min";
    private final String OWM_PRESSURE = "pressure";


    //Open Weather Map Long Term temp set constants   
    private final String OWM_LONG_DAY = "day";
    private final String OWM_LONG_MAX = "max";
    private final String OWM_LONG_MIN = "min";

    //Open Weather Map Wind constants
    private final String OWM_WIND_DEGREE = "deg";
    private final String OWM_WIND_SPEED = "speed";
    
    //Open Weather Map Weather constants
    private final String OWM_WEATHER_DESCRIPTION = "description";
    private final String OWM_WEATHER_ICON = "icon";
    
    public final String TIMEOUT_MESSAGE = "Timeout";

    private final int TIMEOUT_TIME = 5000; //the time it'll take for connection to timeout
    private final int NOT_FOUND_ERROR = 404;
    
    private String query = "?q="; 	//variable query, dependent on location and whether it will be current, short term, or long term

    private String location = ""; //String to contain location
    private URL weatherURL, iconURL;	//URL Object, used to create connection
    private double temp, temp_max, temp_min, windSpeed; //all the variables for CURRENT weather (forecast still needs to be figured out)
    private int pressure, humidity, windDirectionDegree;
    GregorianCalendar time, sunrise, sunset;
    private String weatherDescription, skyState, windDirection; //This may change, need to test out how the currentWeatherSetVariables function is for a while
    private JSONObject allWeatherData, shortTermData, longTermData;
    private ImageIcon icon;

    /**
     * Returns a JSON object to use for obtaining weather data.
     *
     * @param l the string for the location JSON object will be grabbing.
     */
    public JSON(String l) { //When creating the JSON object, initially set the location (eg, London, CA for our city)

        location = l;
        query += location;//first part of the query is always the location. 

    }

    /**
     *
     * Method to initialize and update all current data as specified in the
     * project specifications from the OpenWeatherMap API
     *
     *
     * @return A Current Object with all required fields thrown
     *
     * @throws NoConnectionException Throws this exception whenever the server
     * returns anything other than a 200 (HTTP OK) or 500 (Internal Server
     * Error) Response code, specified timeout message on timeout exception
     * @throws InternalServerError Throws this exception whenever the server
     * returns a 500 response code
     */
    public Current updateCurrentWeatherData() throws NoConnectionException, InternalServerError, BadLocationException{
        Current curr = new Current();
        
        try {
        	//Create a URL to grab weather using the query specified
            weatherURL = new URL(HOST + PATH_CURRENT + query);
            HttpURLConnection connect = (HttpURLConnection) weatherURL.openConnection();
            //Set the timeout time
            connect.setConnectTimeout(TIMEOUT_TIME);

            //If the server returns an error an exception is thrown.
            if (HttpURLConnection.HTTP_INTERNAL_ERROR == connect.getResponseCode()) {
                throw new InternalServerError(connect.getResponseCode() + ": " + connect.getResponseMessage());
            } else if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                throw new NoConnectionException(connect.getResponseCode() + ": " + connect.getResponseMessage());
            }
            
            //Convert the input stream into a string
            InputStream in = connect.getInputStream();
            String jsonString = IOUtils.toString(in);
            
            //create a JSON object using that string
            JSONObject currentWeatherData = new JSONObject(jsonString);
            allWeatherData = new JSONObject(jsonString);
            
            //if the return code in the JSON object is a 404
            if(allWeatherData.getInt(RETURN_CODE) == NOT_FOUND_ERROR)
            	throw new BadLocationException("Error: Improper location");
            
            
            time = (GregorianCalendar) GregorianCalendar.getInstance();
            time.setTimeInMillis(1000 * currentWeatherData.getLong(TIME));
            
            //Calls some functions to set the variables for creating the current object
            JSONObject main = currentWeatherData.getJSONObject(OWM_MAIN);
            JSONArray weather = currentWeatherData.getJSONArray(OWM_WEATHER);
            JSONObject wind = currentWeatherData.getJSONObject(OWM_WIND);
            JSONObject sun = currentWeatherData.getJSONObject(SUN_JSON);
            currentMainSetVariables(main);
            currentWeatherSetVariables(weather);
            currentWindSetVariables(wind);
            currentSunTime(sun);
            //return ADO_Object
            //create the current object
            curr = new Current(time, sunrise, sunset, pressure, windSpeed, temp, temp_min, temp_max, humidity, windDirection, skyState, icon);

        } catch (SocketTimeoutException e) {
            throw new NoConnectionException(TIMEOUT_MESSAGE);
        } catch (IOException e) {
            throw new NoConnectionException("No Connection");
        } catch (JSONException e) {
            System.out.println (e);
            e.printStackTrace();
            throw new InternalServerError("Server cannot process request.");
            
        }
        //return current object
        return curr;
    }

    /**
     * Get the ShortTerm Data from the OpenWeatherMap API. Returns all fields
     * specified by project specifications.
     *
     * @return ShortTerm object initialized with array of 8 hourly objects
     *
     * @throws NoConnectionException Throws this exception whenever the server
     * returns anything other than a 200 (HTTP OK) or 500 (Internal Server
     * Error) Response code, specified timeout message on timeout exception
     * @throws InternalServerError Throws this exception whenever the server
     * returns a 500 response code
     */
    public ShortTerm updateShortTermData() throws NoConnectionException, InternalServerError, BadLocationException{
//	public ShortTerm updateShortTermData(){
        Hourly[] shortTermHourlies = new Hourly[8];
        try {
            //creates the weather URL 
            weatherURL = new URL(HOST + PATH_FORECAST_SHORTTERM + query);

            HttpURLConnection connect = (HttpURLConnection) weatherURL.openConnection();
            connect.setConnectTimeout(TIMEOUT_TIME);//sets timeout

            if (HttpURLConnection.HTTP_INTERNAL_ERROR == connect.getResponseCode()) {
                throw new InternalServerError(connect.getResponseCode() + ": " + connect.getResponseMessage());
            } else if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                throw new NoConnectionException(connect.getResponseCode() + ": " + connect.getResponseMessage());
            }

            //Gets input stream and converts it to string to be handled by JSONObject
            InputStream in = connect.getInputStream();
            String jsonString = IOUtils.toString(in);
            shortTermData = new JSONObject(jsonString);
            
            //if the list returns null (a server error) we call this function recursively to ask the server again
            if(shortTermData.has(ARRAY_JSON) && shortTermData.isNull(ARRAY_JSON))
            	return updateShortTermData();
            else if(shortTermData.getInt(RETURN_CODE) == NOT_FOUND_ERROR) //otherwise if there is no array in the JSON object or the shortTermData is null (for whatever reason) 
            	throw new BadLocationException("Error: Improper location"); //throw a bad location exception
            	
            
            JSONArray arrayData = shortTermData.getJSONArray(ARRAY_JSON);//creates a JSON array with all the tri-hourly separation
            
            //loop to get the 8 hourly objects
            for (int i = 0; i < 8; i++) {
                JSONObject hour = arrayData.getJSONObject(i);
                
                //functions to set global variables 
                shortTermMainSetVariables(hour.getJSONObject(OWM_MAIN));
                shortTermWeatherSetVariables(hour.getJSONArray(OWM_WEATHER));
                shortTermWindSetVariables(hour.getJSONObject(OWM_WIND));
                time = (GregorianCalendar) GregorianCalendar.getInstance();
                time.setTimeInMillis(1000 * hour.getLong(TIME));

                shortTermHourlies[i] = new Hourly(time.get(GregorianCalendar.HOUR_OF_DAY), pressure, windSpeed, temp, temp_min, temp_max, humidity, windDirection, skyState, icon);
            }

        } catch (SocketTimeoutException e) {
            throw new NoConnectionException(TIMEOUT_MESSAGE);
        } catch (IOException e) {
            throw new NoConnectionException("No Connection");
        } catch (JSONException ex)
        {
      //      System.out.println(ex);
      //      ex.printStackTrace();
            
            throw new InternalServerError("Server cannot process request.");
        }
        
        return new ShortTerm(shortTermHourlies);
    }

    /**
     *
     * Get the Long Term Data from the OpenWeatherMap (OWM) API. Returns all
     * fields specified by project specifications. Returns temperature as the
     * "day" field returned from the OWM API
     *
     * @return LongTerm object initialized with 5 Daily objects
     *
     * @throws NoConnectionException Throws this exception whenever the server
     * returns anything other than a 200 (HTTP OK) or 500 (Internal Server
     * Error) Response code, specified timeout message on timeout exception
     * @throws InternalServerError Throws this exception whenever the server
     * returns a 500 response code
     */
    public LongTerm updateLongTermData() throws NoConnectionException, InternalServerError, BadLocationException {
        try {

            Daily[] days = new Daily[5];
            weatherURL = new URL(HOST + PATH_FORECAST_LONGTERM + query + "&cnt=5");
            HttpURLConnection connect = (HttpURLConnection) weatherURL.openConnection();

            connect.setConnectTimeout(TIMEOUT_TIME);
            if (HttpURLConnection.HTTP_INTERNAL_ERROR == connect.getResponseCode()) {
                throw new InternalServerError(connect.getResponseCode() + ": " + connect.getResponseMessage());
            } else if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                throw new NoConnectionException(connect.getResponseCode() + ": " + connect.getResponseMessage());
            }

            InputStream in = connect.getInputStream();
            String jsonString = IOUtils.toString(in);
            JSONObject longTermWeatherData = new JSONObject(jsonString);

            if(longTermWeatherData.has(ARRAY_JSON) && longTermWeatherData.isNull(ARRAY_JSON))
            	return updateLongTermData();
            else if (longTermWeatherData.getInt(RETURN_CODE) == NOT_FOUND_ERROR)
            	throw new BadLocationException("Error: Improper location");
            
            
            JSONArray dailyArray = longTermWeatherData.getJSONArray(ARRAY_JSON);
            String[] weekdays = {"", "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

            longTermData = new JSONObject(jsonString);

            for (int i = 0; i < 5; i++) {
                JSONObject daily = dailyArray.getJSONObject(i);

                longTermTempSetVariables(daily.getJSONObject(OWM_TEMP));
                longTermWeatherSetVariables(daily.getJSONArray("weather"));
                time = (GregorianCalendar) GregorianCalendar.getInstance();
                time.setTimeInMillis(1000 * daily.getLong(TIME));

                int day = time.get(GregorianCalendar.DAY_OF_MONTH);
                int month = time.get(GregorianCalendar.MONTH);

                days[i] = new Daily(weekdays[time.get(GregorianCalendar.DAY_OF_WEEK)] + " " + months[month] + " " + String.valueOf(day), temp, temp_min, temp_max, skyState, icon);
            }
            return new LongTerm(days);		//return ADO_Object

        } catch (SocketTimeoutException e) {
            throw new NoConnectionException(TIMEOUT_MESSAGE);
        } catch (IOException e) {
            throw new NoConnectionException("No Connection");

        } catch (JSONException e) {
            //updateLongTermData(); // Most likely server error, re-fetch data
        }
        return null;
    }

    /**
     * Get the Mars Data from the Mars Weather (marsweather.ingenology.com) API.
     * Returns all fields specified by project specifications.
     *
     * @return Mars object containing initialized parameters from the Mars
     * weather API. Returns average values if Mars weather API returns null
     * values
     *
     * @throws NoConnectionException Throws this exception whenever the server
     * returns anything other than a 200 (HTTP OK) or 500 (Internal Server
     * Error) Response code, specified timeout message on timeout exception
     * @throws InternalServerError Throws this exception whenever the server
     * returns a 500 response code
     */
    public Mars updateMarsData() throws NoConnectionException, InternalServerError, BadLocationException{
        try {
        	//gets URL for current mars weather from the server
        	URL marsURL = new URL("http://marsweather.ingenology.com/v1/latest/?format=json");
            HttpURLConnection connect = (HttpURLConnection) marsURL.openConnection();
            connect.setConnectTimeout(TIMEOUT_TIME);

            
            //use the same type of response code checking as the other update methods
            if (HttpURLConnection.HTTP_INTERNAL_ERROR == connect.getResponseCode()) {
                throw new InternalServerError(connect.getResponseCode() + ": " + connect.getResponseMessage());
            } else if (HttpURLConnection.HTTP_OK != connect.getResponseCode()) {
                throw new NoConnectionException(connect.getResponseCode() + ": " + connect.getResponseMessage());
            }

            //Convert unput stream to a string
            InputStream in = connect.getInputStream();
            String jsonString = IOUtils.toString(in);
            
            //convert string to JSON object
            JSONObject marsWeatherData = new JSONObject(jsonString).getJSONObject("report");
            //call method to set all variables
            currentMarsSetVariables(marsWeatherData);

            return new Mars(pressure, windSpeed, temp, temp_min, temp_max, humidity, windDirection, skyState, null);

        } catch (SocketTimeoutException e) {
            throw new NoConnectionException(TIMEOUT_MESSAGE);
        } catch (IOException e) {
            throw new NoConnectionException("No Connection");
        } catch (JSONException e) {
            //updateLongTermData(); // Most likely server error, re-fetch data
        }

        return null;
    }

    /**
     * Method to set temperature, sky state, humidity, windDirection, and
     * pressure fields for the Mars Object
     *
     * @param mars JSONObject that contains required information
     */
    private void currentMarsSetVariables(JSONObject mars) {

    	/*
    	 * In this method we check if every variable is null, if it is
    	 * the average value for mars is returned. If no value is found for 
    	 * the average it is returned as null or 0 (such as humidity and windspeed)
    	 */
    	
        if (!mars.isNull("ls")) {
            temp = mars.getDouble("ls");
        } else {
            temp = -55.0;
        }

        if (!mars.isNull("min_temp")) {
            temp_min = mars.getDouble("min_temp");
        } else {
            temp_min = -133.0;
        }

        if (!mars.isNull("max_temp")) {
            temp_max = mars.getDouble("max_temp");
        } else {
            temp_max = 27.0;
        }

        if (!mars.isNull("atmo_opacity")) {
            skyState = mars.getString("atmo_opacity");
        } else {
            skyState = null;
        }

        if (!mars.isNull("wind_speed")) {
            windSpeed = mars.getDouble("wind_speed");
        } else {
            windSpeed = 0.0;
        }

        if (!mars.isNull("abs_humidity")) {
            humidity = mars.getInt("abs_humidity");
        } else {
            humidity = 0;
        }

        if (!mars.isNull("wind_direction")) {
            windDirection = mars.getString("wind_direction");
        } else {
            windDirection = "--";
        }

        if (!mars.isNull("pressure")) {
            pressure = mars.getInt("pressure");
        } else {
            pressure = 6;
        }

        
    }

    /**
     * Private method to set all temperature, humidity, and pressure fields for
     * current objects
     *
     * @param main JSONObject containing these fields.
     */
    private void currentMainSetVariables(JSONObject main) {
        //grab all the variables from the JSON object and store them accordingly
    	humidity = main.getInt(OWM_HUMIDITY);
        pressure = main.getInt(OWM_PRESSURE);
        temp_max = main.getDouble(OWM_TEMP_MAX);
        temp_min = main.getDouble(OWM_TEMP_MIN);
        temp = main.getDouble(OWM_TEMP);
    }

    /**
     * Private method to set all temperature, humidity, and pressure fields for
     * ShortTerm objects
     *
     * @param main JSONObject containing these fields
     */
    private void shortTermMainSetVariables(JSONObject main) {
    	//grab all variables from the JSON object
        humidity = main.getInt(OWM_HUMIDITY);
        pressure = main.getInt(OWM_PRESSURE);
        temp_max = main.getDouble(OWM_TEMP_MAX);
        temp_min = main.getDouble(OWM_TEMP_MIN);
        temp = main.getDouble(OWM_TEMP);
    }

    /**
     * Private method to set all temperature fields for LongTerm Object
     *
     * @param temperature JSONObject containing temperature data
     */
    private void longTermTempSetVariables(JSONObject temperature) {
    	
        temp = temperature.getDouble(OWM_LONG_DAY);
        temp_max = temperature.getDouble(OWM_LONG_MAX);
        temp_min = temperature.getDouble(OWM_LONG_MIN);
    }

    /**
     * Private method for weather variables. Gets sky state and weather
     * description as well as ImageIcon for skystate
     *
     * @param weather JSONArray containing all weather fields
     */
    private void currentWeatherSetVariables(JSONArray weather) {
    	//weather is always returned as a list, so we obtain the forst JSON object in the list
        JSONObject weatherData = weather.getJSONObject(0);
        
        //Methods to get weather description and sky-state
        weatherDescription = weatherData.getString(OWM_WEATHER_DESCRIPTION);
        skyState = weatherData.getString(OWM_MAIN);

        //Uses a URL to grab an ImageIcon from the OWM api
        try {
            iconURL = new URL(HOST + PATH_ICON + weatherData.getString(OWM_WEATHER_ICON) + ".png");
            icon = new ImageIcon(iconURL);
        } catch (Exception e) {
        	icon = null;
        }

    }

    private void shortTermWeatherSetVariables(JSONArray weather) {

    	//weather is always returned as a list, so we obtain the forst JSON object in the list
        JSONObject weatherData = weather.getJSONObject(0);
        
        //Methods to get weather description and sky-state
        weatherDescription = weatherData.getString(OWM_WEATHER_DESCRIPTION);
        skyState = weatherData.getString(OWM_MAIN);

        //Uses a URL to grab an ImageIcon from the OWM api
        try {
            iconURL = new URL(HOST + PATH_ICON + weatherData.getString(OWM_WEATHER_ICON) + ".png");
            icon = new ImageIcon(iconURL);
        } catch (Exception e) {
        
        }
    }

    private void longTermWeatherSetVariables(JSONArray weather) {
    	//weather is always returned as a list, so we obtain the forst JSON object in the list
        JSONObject weatherData = weather.getJSONObject(0);
        
        //Methods to get weather description and sky-state
        weatherDescription = weatherData.getString(OWM_WEATHER_DESCRIPTION);
        skyState = weatherData.getString(OWM_MAIN);

        //Uses a URL to grab an ImageIcon from the OWM api
        try {
            iconURL = new URL(HOST + PATH_ICON + weatherData.getString(OWM_WEATHER_ICON) + ".png");
            icon = new ImageIcon(iconURL);
        } catch (Exception e) {
        
        }
    }

    /**
     * Private method that gets wind data and converts the degree to a rough
     * direction
     *
     * @param wind JSONObject containing all wind data
     */
    private void currentWindSetVariables(JSONObject wind) {
        windDirectionDegree = wind.getInt(OWM_WIND_DEGREE);
        windSpeed = wind.getDouble(OWM_WIND_SPEED);
        String[] direction = {"North", "North-Northeast", "Northeast", "East-Northeast", "East", "East-Southeast", "Southeast", "South-Southeast", "South", "South-Southwest", "Southwest", "West-Southwest", "West", "West-Northwest", "Northwest", "North-Northwest"};

        windDirection = direction[(int) ((windDirectionDegree / 22.5) % 16)];
    }

    /**
     * method to set the variables for shortTerm wind
     *
     * @param wind JSONObject that conatins all wind variables required.
     */
    private void shortTermWindSetVariables(JSONObject wind) {
        windDirectionDegree = wind.getInt(OWM_WIND_DEGREE);
        windSpeed = wind.getDouble(OWM_WIND_SPEED);
     
        //method to grab 
        String[] direction = {"North", "North-Northeast", "Northeast", "East-Northeast", "East", "East-Southeast", "Southeast", "South-Southeast", "South", "South-Southwest", "Southwest", "West-Southwest", "West", "West-Northwest", "Northwest", "North-Northwest"};

        windDirection = direction[(int) ((windDirectionDegree / 22.5) % 16)];
    }

    /**
     * Private method that gets data for sunrise and sunset and sets it
     *
     * @param sun JSONObject for sunrise and sunset data
     */
    private void currentSunTime(JSONObject sun) {
        sunrise = (GregorianCalendar) GregorianCalendar.getInstance();
        sunset = (GregorianCalendar) GregorianCalendar.getInstance();

        sunrise.setTimeInMillis(1000 * sun.getLong("sunrise"));
        sunset.setTimeInMillis(1000 * sun.getLong("sunset"));

    }


}
