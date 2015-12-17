package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.entity.Place;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by devraj.singh on 9/26/15.
 */
public class LocationFinder {

    private static String templateApi = "https://maps.googleapis.com/maps/api/geocode/json?address=$location&key=$API_KEY&language=en";

    public static Place getPlaceGivenLocationName(String locationName) throws HttpResponseFailedException, InvalidResponseException {
        String api = templateApi;
        api = api.replace("$location",locationName);
        api = api.replace("$key",KeyProvider.getGoogleApiKey());
        CustomResponse response = HttpHelper.httpget(api);
        if(response==null)
            throw new HttpResponseFailedException("Http Response is null");
        if(response.getResponseCode()!=200)
            throw new InvalidResponseException("StatusCode is NOT 200, statusCode="+response.getResponseCode());

        String resultJson = response.getContent();
        Place place = processJson(resultJson);
        return place;
    }

    private static Place processJson(String resultJson) throws InvalidResponseException {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(resultJson).getAsJsonObject().getAsJsonArray("");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        if(status.equalsIgnoreCase("ZERO_RESULTS"))
            return null;
        if(!status.equalsIgnoreCase("ok"))
        {
            throw new InvalidResponseException("Status Code is NOT OK, statusCode="+status);
        }

        JsonObject resultJsonObject = jsonObject.getAsJsonArray("results").get(0).getAsJsonObject();
        String formattedAddress=resultJsonObject.get("formatted_address").getAsString();
        String shortName = resultJsonObject.getAsJsonArray("address_components").get(0).getAsJsonObject().get("short_name").getAsString();
        JsonObject latLong = resultJsonObject.getAsJsonObject("geometry").getAsJsonObject("location");
        double lattitude = Double.parseDouble(latLong.get("lat").getAsString());
        double longitude = Double.parseDouble(latLong.get("lng").getAsString());
        Location location = new Location(lattitude,longitude);
        return new Place(shortName,location,formattedAddress);
    }
}
