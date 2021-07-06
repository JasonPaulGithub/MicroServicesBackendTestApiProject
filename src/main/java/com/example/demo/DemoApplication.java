package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    public class restController {
        /**
         * Post Request
         * How to use: Swap the interval (i.e: "10") in the CURL below to the required value:

         curl -X POST -H "Content-type: application/json" -d "10" "http://localhost:8080/postbody"

         **/
        @PostMapping("/postbody")
        public String postBody(@RequestBody int period) throws IOException {
            return "For a Period of " + period + ": The (Average) Result is " + getInterval(period);
        }
    }

    public String getInterval(int interval) throws IOException {

        // Connect to get the data from URL
        URL url = new URL("<URL GOES HERE>");
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
        Map<String, JSONArray> nestedKeys = new HashMap<>();
        List<String> nestedReference = new ArrayList<>();

        while (keys.hasNext()) {
            String key = keys.next();
            if (ssz.get(key) instanceof JSONArray) {
                nestedKeys.put(key, ssz.getJSONArray(key));
                nestedReference.add(key);
            }
        }

        // Get the average value for each interval
        double totalSumOfValues = 0;
        int entries = 0;

        for (String reference : nestedReference) {
            JSONArray targetArray = nestedKeys.get(reference);
            for (int x = 0; x < nestedKeys.size(); x++) {
                for (int y = interval; y <= targetArray.length(); y = y + interval) {
                    try {
                        //System.out.println("Entry = " + targetArray.get(y));
                        String s = String.valueOf(targetArray.get(y));
                        double d = Double.parseDouble(s);
                        totalSumOfValues = totalSumOfValues + d;
                        entries++;
                    } catch (Exception s) {
                        //System.out.println(s);
                    }
                }
            }
        }

        return String.valueOf(totalSumOfValues / entries);
    }

}
