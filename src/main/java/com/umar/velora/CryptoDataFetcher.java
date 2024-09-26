package com.umar.velora;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CryptoDataFetcher {

    public Map<String, String> getCryptoPrices(String[] cryptoIds, String currency) {
        Map<String, String> prices = new HashMap<>();
        try {
            String ids = String.join(",", cryptoIds);
            String vsCurrency = currency.toLowerCase();
            String urlStr = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=" + vsCurrency;
            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Parse the response
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(request.getInputStream())).getAsJsonObject();

            for (String id : cryptoIds) {
                if (jsonObject.has(id)) {
                    JsonObject priceObject = jsonObject.getAsJsonObject(id);
                    if (priceObject.has(vsCurrency)) {
                        String price = priceObject.get(vsCurrency).getAsString();
                        prices.put(id, price);
                    } else {
                        prices.put(id, "N/A");
                    }
                } else {
                    prices.put(id, "N/A");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prices;
    }
}
