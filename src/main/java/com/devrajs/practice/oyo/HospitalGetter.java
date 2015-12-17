package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Hospital;
import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by devraj.singh on 9/27/15.
 *
 * This class will find all hospitals nearest to given location
 */
public class HospitalGetter {

    //static String templateApi = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$longitude,$latitude&radius=30000&types=hospital&key=AIzaSyBN11RR9fGE04ngA9GnYaN8wJU8Sntv_ss";
    static String api="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lattitude,$longitude&radius=$radius&types=hospital&key=$key";

    static Logger logger = LoggerFactory.getLogger(Hospital.class);

    private static final int RADIUS = 10000;
    public static double getNearestHospitalDistance(double latitude,double longitude) throws InvalidResponseException, HttpResponseFailedException {
        List<Hospital> hospitalList = findAllHospitalForGivenLocation(latitude,longitude,RADIUS);
        return calculateNearestHospitalDistance(hospitalList, new Location(latitude,longitude));
    }

    private static String getResultJson(double latitude, double longitude,int radius) throws HttpResponseFailedException, InvalidResponseException {
        String finalApi = api;
        finalApi = finalApi.replace("$key",KeyProvider.getGoogleApiKey());
        finalApi = finalApi.replace("$lattitude",String.valueOf(latitude));
        finalApi = finalApi.replace("$longitude",String.valueOf(longitude));
        finalApi = finalApi.replace("$radius",String.valueOf(radius));

        logger.debug("After replacing all macros, api: {}",finalApi);

        CustomResponse response = HttpHelper.httpget(finalApi);
        if(response==null) {
            throw new HttpResponseFailedException("Response recieved is null");
        }
        if(response.getResponseCode()!=200) {
            throw new InvalidResponseException("Response code is not 200, statusCode="+response.getResponseCode());
        }
        String resultJson = response.getContent();
        logger.debug("Result Json: {}",resultJson);
        return resultJson;
    }

    public static List<Hospital> findAllHospitalForGivenLocation(double lattitude,double longitude, int radius) throws InvalidResponseException, HttpResponseFailedException {
        logger.debug("Finding all hospital for longitude:{} and lattitude:{}", longitude,lattitude);
        String resultJson = getResultJson(lattitude,longitude,radius);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonEle = jsonParser.parse(resultJson);
        JsonArray jsonArray = jsonEle.getAsJsonObject().getAsJsonArray("results");
        String status = jsonEle.getAsJsonObject().get("status").getAsString();
        if(!status.equalsIgnoreCase("ok"))
            throw new HttpResponseFailedException("status!=\"OK\", status="+status);
        List<Hospital> hospitalList = new ArrayList<>();
        Iterator<JsonElement> iterator = jsonArray.iterator();

        while (iterator.hasNext())
        {
            JsonElement jsonElement = iterator.next();
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            double hotelLattitude = Double.parseDouble(jsonElement.getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsString());
            double hotelLongitude = Double.parseDouble(jsonElement.getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsString());
            Hospital hospital = new Hospital(name,hotelLattitude,hotelLongitude);
            hospitalList.add(hospital);
        }
        return hospitalList;
    }

    private static Hospital calculateNearestHospital(List<Hospital> hospitalList, Location currentLocation) throws InvalidResponseException, HttpResponseFailedException {
        Hospital nearestHospital = null;
        int minDistance= Integer.MAX_VALUE;
        for(Hospital hospital : hospitalList)
        {
            Location location = hospital.getLocation();
            int distance = (int)DistanceCalculator.calculateDistanceBetweenTwoLocation(location,currentLocation);
            if(distance < minDistance)
            {
                minDistance = distance;
                nearestHospital = hospital;
            }
        }
        return nearestHospital;
    }

    private static int calculateNearestHospitalDistance(List<Hospital> hospitalList, Location currentLocation) throws InvalidResponseException, HttpResponseFailedException {
        int minDistance= Integer.MAX_VALUE;
        for(Hospital hospital : hospitalList)
        {
            Location location = hospital.getLocation();
            int distance = (int)DistanceCalculator.calculateDistanceBetweenTwoLocation(location,currentLocation);
            if(distance < minDistance)
            {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    public static Hospital getNearestHospital(Location location) throws InvalidResponseException, HttpResponseFailedException {
        List<Hospital> hospitalList= findAllHospitalForGivenLocation(location.getLattitude(),location.getLongitude(),RADIUS);
        Hospital hospital = calculateNearestHospital(hospitalList,location);
        return hospital;
    }
}

