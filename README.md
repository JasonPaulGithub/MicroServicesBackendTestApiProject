# MicroServicesBackendTestProject

Given an API URL that provides JSON Data (example in the API RESPONSE DATA document), our service will connect to this endpoint and collect data over a three hour time period.
Based on the interval given we will gather up an average and procide this as a response.

Simply run this Curl command after you run the service:

> curl -X POST -H "Content-type: application/json" -d "10" "http://localhost:8080/postbody"

You can replace the "10" with the interval you wish to use.
