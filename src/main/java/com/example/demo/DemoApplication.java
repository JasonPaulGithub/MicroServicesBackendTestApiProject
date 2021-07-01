package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

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
        JSONArray targetArray = nestedArrays.get("3000");
        for (int i = interval; i <= targetArray.length(); i = i + interval) {
            System.out.println(targetArray.get(i));
            // return an average value
        }

    }

    /**
     * Get Request
     **/
    @RestController
    public class restController {

        @GetMapping("/hello")
        public String getHello() {
            return "Hello";
        }

        /**
         * Post Request
         **/
        @RestController
        public class postHello {
            @PostMapping("/hello")
            public String postHello() {
                return "Hello";
            }
        }
    }
}