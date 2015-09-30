package com.mycompany;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import redis.clients.jedis.Jedis;

public class WeatherService {
    private Jedis jedis;

    public WeatherService() {
        jedis = new Jedis("localhost");
    }
    
    public void weatherOf(String city) throws Exception {
        JsonElement weatherData = getDataFor(city);

        double temperature = getTemperatureFrom(weatherData);
        String desc = getDescriptionFrom(weatherData);
        
        System.out.println(desc + ", temperature "+temperature+ " celcisus");        
    }
    
    private JsonElement getDataFor(String city) throws Exception {
        // etsitään kaupungin city säätietoja rediksestä
        String weatherInfo = jedis.get(city);
        
        // jos ei löytyny
        if (weatherInfo==null) {
            // haetaan tiedot internetistä
            weatherInfo = readFromUrl("http://api.openweathermap.org/data/2.5/weather?q="+city);
            
            // ja talletetaan ne redisiin minuutiksi
            jedis.set(city, weatherInfo);
            jedis.expire(city, 60);
        } 
        
        return new JsonParser().parse(weatherInfo);    
    }

    protected String getDescriptionFrom(JsonElement weatherData){
        return weatherData.getAsJsonObject().get("weather").getAsJsonArray().get(0)
                .getAsJsonObject().get("description").getAsString();    
    }   
    
    protected double getTemperatureFrom(JsonElement weatherData){
        return weatherData.getAsJsonObject().get("main")
                .getAsJsonObject().get("temp").getAsDouble()-273.15;        
 
    }    
    protected String readFromUrl(String url) throws IOException {
        Scanner reader = new Scanner(new URL(url).openStream());
        return reader.nextLine();
    }
}
