package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Airport;
import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by devraj.singh on 9/27/15.
 */
public class AiportFinder {

    private static String api = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lattitude,$longitude&radius=$radius&types=airport&key=$key";
    private static final int RADIUS = 100000;//in meter

    static Logger logger = LoggerFactory.getLogger(AiportFinder.class);

    public static Airport findNearestMainAirport(Location location) throws HttpResponseFailedException, InvalidResponseException {
        String finalApi = api;

        logger.debug("Template api for finding airport: {}",api);

        finalApi = finalApi.replace("$lattitude",String.valueOf(location.getLattitude()));
        finalApi = finalApi.replace("$longitude",String.valueOf(location.getLongitude()));
        finalApi = finalApi.replace("$radius",String.valueOf(RADIUS));
        finalApi = finalApi.replace("$key",KeyProvider.getGoogleApiKey());
        logger.debug("after replacing all macros , api is: {}",finalApi);
        CustomResponse response = HttpHelper.httpget(finalApi);
        if(response==null)
            throw new HttpResponseFailedException("Response returned is null");
        String resultJson = response.getContent();

        logger.trace("result json: {}", resultJson);

        Airport nearestMainAirport = processJson(resultJson);
        if(nearestMainAirport != null) {
            logger.info("Nearest AirportName: {}",nearestMainAirport.getName());
            logger.debug("Nearest Airport: {}", nearestMainAirport.toString());
        }
        return nearestMainAirport;
    }

    public int findNearestMainAirportDistance(Location location) throws InvalidResponseException, HttpResponseFailedException {
        Airport airport = findNearestMainAirport(location);
        Location airportLocation = airport.getLocation();
        int distance = (int) DistanceCalculator.calculateDistanceBetweenTwoLocation(location,airportLocation);
        return distance;
    }

    private static Airport processJson(String resultJson) throws InvalidResponseException {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(resultJson);
        String status = jsonElement.getAsJsonObject().get("status").getAsString();
        if(status.equalsIgnoreCase("ZERO_RESULTS"))
            return null;
        else if(!status.equalsIgnoreCase("ok") )
        {
            throw new InvalidResponseException("Status!=OK, status="+status);
        }

        JsonObject firstresult = jsonElement.getAsJsonObject().getAsJsonArray("results").get(0).getAsJsonObject();
        JsonObject latLong = firstresult.getAsJsonObject("geometry").getAsJsonObject("location");
        double lattitude = Double.parseDouble(latLong.get("lat").getAsString());
        double longitutude = Double.parseDouble(latLong.get("lng").getAsString());
        String name = firstresult.get("name").getAsString();
        String vicinity = firstresult.get("vicinity").getAsString();

        return new Airport(name,lattitude,longitutude,vicinity);
    }
}
