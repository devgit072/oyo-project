package com.devrajs.practice.oyo.entity;

import com.devrajs.practice.oyo.AiportFinder;
import com.devrajs.practice.oyo.HospitalGetter;
import com.devrajs.practice.oyo.ScoreCalculator;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by devraj.singh on 9/26/15.
 */
public class Hotel {

    @Setter @Getter private String name;
    @Setter @Getter private String hotelId;
    @Setter @Getter private Location location;
    @Setter @Getter private Price price;
    @Setter @Getter private Airport nearestAirport;
    @Setter @Getter private Hospital nearestHospital;
    @Setter @Getter private double distanceFromSearchedLocation;
    @Setter @Getter private Score score;

    public Hotel(String name, String hotelId,Location location, Price price, Airport nearestAirport, Hospital nearestHospital)
    {
        this.name=name;
        this.hotelId=hotelId;
        this.location=location;
        this.price=price;
        this.nearestAirport=nearestAirport;
        this.nearestHospital=nearestHospital;
    }
    public Hotel(String name, String hotelId,Location location, Price price) throws InvalidResponseException, HttpResponseFailedException {
        this.name=name;
        this.hotelId=hotelId;
        this.location=location;
        this.price=price;
        nearestAirport=AiportFinder.findNearestMainAirport(location);
        nearestHospital = HospitalGetter.getNearestHospital(location);
        score = ScoreCalculator.calculateScore(price.getAveragePrice(),location,nearestAirport.getLocation(),nearestHospital.getLocation());
    }

    @Override
    public String toString()
    {
        String str=String.format("Name=%s, hotelId=%s, location=%s, price=%s, Nearest_AirPort=[%s], Nearest_Hospital=[%s]",name,hotelId,location,price.getAveragePrice(),nearestAirport.getName(),nearestHospital.getName());
        return str;
    }
}
