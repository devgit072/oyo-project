package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.Location;
import com.devrajs.practice.oyo.entity.Score;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;

/**
 * Created by devraj.singh on 9/27/15.
 */
public class ScoreCalculator {

    public static Score calculateScore(double hotelPrice, Location hotelLocation, Location airportLocation, Location hospitalLocation) throws InvalidResponseException, HttpResponseFailedException {
        return new Score(getPriceScore(hotelPrice),getAirportScore(hotelLocation,airportLocation),getHospitalScore(hotelLocation,hospitalLocation));
    }

    public static double getPriceScore(double hotelAvgPrice)
    {
        double score = 20*((4000-hotelAvgPrice)/4000);
        if(score<0)
            return 0;
        if(score>20)
            return 20;
        return score;
    }

    private static double getAirportScore(Location hotelLocation, Location airportLocation) throws InvalidResponseException, HttpResponseFailedException {
        double distanceFromAirport= DistanceCalculator.calculateDistanceBetweenTwoLocation(hotelLocation, airportLocation);
        double score = 30*((3-distanceFromAirport)/3);
        if(score<0)
            return 0;
        else if(score>30)
            return 30;
        return score;
    }
    private static double getHospitalScore(Location hotelLocation, Location hospitalLocation) throws InvalidResponseException, HttpResponseFailedException {
        double nearestHospitalDistance=DistanceCalculator.calculateDistanceBetweenTwoLocation(hotelLocation,hospitalLocation);
        double score = 30*((3-nearestHospitalDistance)/3);
        if(score<0)
            return 0;
        else if(score>30)
            return 30;
        return score;
    }
}

