import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Service {
    String country;
    String currencyCode;
    String countryCode;
    String API_KEY = "1efc05f8c068f443b4d82d6678698c08";

    public Service(String country){
        this.country=country;
        Locale.setDefault(Locale.ENGLISH);

        for (Locale locale : Locale.getAvailableLocales())
            if(locale.getDisplayCountry().equals(country))
                currencyCode=Currency.getInstance(locale).getCurrencyCode();


        for(String code : Locale.getISOCountries())
            if(new Locale("", code).getDisplayCountry().equals(country)) {countryCode=code;}

        System.out.println(countryCode);
        System.out.println(currencyCode);
    }

    public String getWeather(String city){
        StringBuilder json = new StringBuilder();
        try {
            URL weatherURL = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city +","+countryCode+ "&appid="+API_KEY);
            System.out.println(weatherURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weatherURL.openStream(), StandardCharsets.UTF_8));
            String line;
            while ((line=bufferedReader.readLine())!=null)
                json.append(line);

        }catch (Exception e) {
            e.printStackTrace();
            return "Incorrect data";
        }
        return json.toString();
    }

    public Double getRateFor(String currencyCode){
        double resultRate=-1.0;

        try {
            URL exchangeUrl = new URL("https://api.exchangerate.host/latest?base="+currencyCode+"&symbols="+this.currencyCode);

            StringBuilder json = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchangeUrl.openStream(), StandardCharsets.UTF_8));
            String line;

            while ((line=bufferedReader.readLine())!=null)
                json.append(line);

            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(json.toString());
            JsonObject rates = (JsonObject) object.get("rates");

            resultRate = Double.parseDouble(rates.get(this.currencyCode).toString());


        } catch (Exception e ){
            e.printStackTrace();
        }

        return resultRate;
    }

    public Double getNBPRate(){
        double resultRate =1.0;

        try{
            if(country.equals("Poland"))
                return resultRate;

            URL nbpUrl = new URL("http://api.nbp.pl/api/exchangerates/rates/a/"+this.currencyCode+"/?format=json");

            StringBuilder json = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(nbpUrl.openStream(), StandardCharsets.UTF_8));
            String line;

            while ((line=bufferedReader.readLine())!=null)
                json.append(line);


            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(json.toString());
            JsonArray rates = (JsonArray) object.get("rates");
            JsonObject ratesObject = (JsonObject)rates.get(0);
            JsonPrimitive mid = (JsonPrimitive) ratesObject.get("mid");

            resultRate = Double.parseDouble(mid.toString());
            return resultRate;

        }catch (Exception e){ e.printStackTrace(); }

        return resultRate;
    }
}