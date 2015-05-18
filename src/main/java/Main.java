/**
 * CS2212 
 * 6_TheWeather
 * 
 * Main.java
 * A program that fetches weather data based on the OpenWeatherMapAPI.
 * Displays the weather forecast for current weather, short term (8x 3 hour intervals)
 * and long-term (5 days).
 * Also uses the MAAS API to fetch data from the rover on Mars, for the Mars
 * weather information.
 * 
 * @author team6
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GUIWindow window = new GUIWindow();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });
    }
    
}
