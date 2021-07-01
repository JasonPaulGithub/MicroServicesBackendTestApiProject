package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Connect to get the data from URL
        URL url = new URL("https://reference.intellisense.io/test.dataprovider");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        // int status = con.getResponseCode();
        // System.out.println(status);

        // We now read the data as string
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        // Convert the resulting String Builder to Json Object
        JSONObject paymentRecord = new JSONObject(content.toString());
        // For sake of brevity let's assume 660 is a permanent code value for a variety of nested values
        JSONObject ssz = paymentRecord.getJSONObject("660");

        // Extract the Keys
        Iterator<String> keys = ssz.keys();
        Map<String, JSONArray> nestedArrays = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            if (ssz.get(key) instanceof JSONArray) {
                nestedArrays.put(key, ssz.getJSONArray(key));
            }
        }

        // Get the average value for each interval
        int interval = 60;
        JSONArray targetArray = nestedArrays.get("3000"); //todo: loop
        double totalSumOfValues = 0;
        int enteries = 0;
        for (int x = interval; x <= targetArray.length(); x = x + interval) {
            try {
                System.out.println("Entry = " + targetArray.get(x));
                String s = String.valueOf(targetArray.get(x));
                double d = Double.parseDouble(s);
                totalSumOfValues = totalSumOfValues + d;
                enteries++;
            } catch (Exception s) {
                System.out.println(s);
            }
        }
        System.out.println(totalSumOfValues);
        System.out.println("Average = " + totalSumOfValues / enteries);
    }


    @RestController
    public class restController {
        @GetMapping("/hello")
        public String getHello() {
            return "Hello";
        }

        /**
         * Post Request
         *
         * How to use: Swap the interval with the "10" to what ever is required.
         *
          curl -X POST -H "Content-type: application/json" -d "10" "http://localhost:8080/postbody"
         **/
        @PostMapping("/postbody")
        public String postBody(@RequestBody String period) {
            return "For a Period of " + period + ": The (Average) Result is " + period;
        }
    }

}
