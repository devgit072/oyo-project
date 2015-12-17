package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Hotel;
import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.entity.Price;
import com.devrajs.practice.oyo.entity.SortingCriteria;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by devraj.singh on 12/12/15.
 */
public class ResultFormatter {

    public static String formJsonResponse(List<Hotel> list , SortingCriteria sortingCriteria, int radius)
    {
        int totalResult = list.size();
        String sortingCriteriaStr = sortingCriteria.name();
        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("total_result", totalResult);
        mainObject.addProperty("sorting_criteria", sortingCriteriaStr);
        mainObject.addProperty("radius",radius);
        JsonArray hotelResults = getHotelJsonArray(list);
        mainObject.add("results",hotelResults);

        return mainObject.toString();
    }

    static JsonArray getHotelJsonArray(List<Hotel> list)
    {
        JsonArray jsonArray = new JsonArray();
        for(Hotel hotel : list)
        {
            JsonObject hotelObject = getHotelObject(hotel);
            jsonArray.add(hotelObject);
        }
        return jsonArray;
    }

    static JsonObject getHotelObject(Hotel hotel)
    {
        JsonObject hotelObject = new JsonObject();
        hotelObject.addProperty("name",hotel.getName());
        hotelObject.add("location", getLocationObject(hotel.getLocation()));
        hotelObject.add("price",getPriceObject(hotel.getPrice()));
        hotelObject.addProperty("nearest_airport",hotel.getNearestAirport().getName());
        hotelObject.addProperty("nearest_hospital",hotel.getNearestHospital().getName());
        return hotelObject;
    }

    static JsonObject getLocationObject(Location location)
    {
        JsonObject locationObject = new JsonObject();
        locationObject.addProperty("lattittude",location.getLattitude());
        locationObject.addProperty("longitude",location.getLongitude());
        return locationObject;
    }
    static JsonObject getPriceObject(Price price)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sinle_bed",price.getSingleBedPrice());
        jsonObject.addProperty("double_bed",price.getDoubleBedPrice());
        jsonObject.addProperty("half_day",price.getHalfDayExtraPrice());
        return jsonObject;
    }
}
