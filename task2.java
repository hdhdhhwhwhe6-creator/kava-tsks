// WeatherFetcher.java
// A Java program that fetches and parses weather data using a REST API

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject; // Use org.json library for parsing JSON

public class WeatherFetcher {

    public static void main(String[] args) {
        // Coordinates for Delhi, India
        double latitude = 28.61;
        double longitude = 77.23;

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                           "&longitude=" + longitude + "&current_weather=true";

        try {
            // Create the URL and open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject json = new JSONObject(response.toString());
            JSONObject currentWeather = json.getJSONObject("current_weather");

            // Display structured data
            System.out.println("📍 Location: Delhi, India");
            System.out.println("🌡️ Temperature: " + currentWeather.getDouble("temperature") + " °C");
            System.out.println("💨 Wind Speed: " + currentWeather.getDouble("windspeed") + " km/h");
            System.out.println("🧭 Wind Direction: " + currentWeather.getDouble("winddirection") + "°");
            System.out.println("⏱️ Time: " + currentWeather.getString("time"));

        } catch (Exception e) {
            System.out.println("❌ Error fetching weather data: " + e.getMessage());
        }
    }
}
