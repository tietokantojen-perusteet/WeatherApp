package com.mycompany;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        WeatherService weather = new WeatherService();
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print("kaupunki: ");
            String city = reader.nextLine();
            if ( city.isEmpty() ){
                break;
            }
            weather.weatherOf(city);
        }
    }
}
