package com.devrajs.practice.oyo.service;

import com.devrajs.practice.oyo.HotelGetter;
import com.google.gson.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by devraj.singh on 12/16/15.
 */

@Path("/")
public class GetHotelList {

    @GET
    @Path("/getOyoHotels")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOyoHotels(
            @QueryParam("sortby") String sortBy,
            @QueryParam("latti") double latti,
            @QueryParam("longi") double longi
    )
    {
        try {
            HotelGetter hotelGetter = new HotelGetter();
            String jsonResponse = hotelGetter.getHotelListInJson(latti, longi, sortBy);
            return jsonResponse;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String exceptionMsg = prepareExceptionMessage(e);
            return exceptionMsg;
        }
    }

    private String prepareExceptionMessage(Exception e)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("result", "technical exception met");
        jsonObject.addProperty("exception_reason", e.getMessage());
        return jsonObject.toString();
    }
}
