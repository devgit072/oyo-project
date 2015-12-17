package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by devraj.singh on 10/21/15.
 */
public class DistanceCalculator {

    static Logger logger = LoggerFactory.getLogger(DistanceCalculator.class);

    public static double calculateDistanceBetweenTwoLocation(double latti1,double longi1,double latti2,double longi2) throws HttpResponseFailedException, InvalidResponseException {
        // calculate distance in meter between two location
        String api = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=$sourceLattitude,$sourceLongitude&destinations=$targetLattitude,$targetLongitude&mode=driving&key=$key";
        api = api.replace("$sourceLattitude",String.valueOf(latti1));
        api = api.replace("$sourceLongitude",String.valueOf(longi1));
        api = api.replace("$targetLattitude",String.valueOf(latti2));
        api = api.replace("$targetLongitude",String.valueOf(longi2));
        api = api.replace("$key",KeyProvider.getGoogleApiKey());

        logger.debug("after replacing all macros, api: {}",api);

        CustomResponse response = HttpHelper.httpget(api);
        if(response.getResponseCode()!=200)
            throw new HttpResponseFailedException("Non 200 response...:(");
        String jsonResponse = response.getContent();
        logger.debug("JsonResponse: {}", jsonResponse);
        double distance = processJson(jsonResponse);
        logger.info("Distance calculated: {}",distance);
        return distance;
    }

    public static double calculateDistanceBetweenTwoLocation(Location location1, Location location2) throws InvalidResponseException, HttpResponseFailedException {
        return calculateDistanceBetweenTwoLocation(location1.getLattitude(),location1.getLongitude(),location2.getLattitude(),location2.getLongitude());
    }

    static private double processJson(String jsonResponse) throws InvalidResponseException {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonResponse);
        String status = jsonElement.getAsJsonObject().get("status").getAsString();
        if(!status.equalsIgnoreCase("OK"))
            throw new InvalidResponseException("Status not OK, status="+status);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String value = jsonObject.getAsJsonArray("rows").get(0).getAsJsonObject().getAsJsonArray("elements").get(0).getAsJsonObject().get("distance").getAsJsonObject().get("text").getAsString();
        String[] arr = value.split(" ");
        double distance = Double.valueOf(arr[0]);
        return distance;
    }
}
