package com.devrajs.practice.oyo.entity;

import lombok.Getter;

/**
 * Created by devraj.singh on 10/23/15.
 */
public class Location {

    @Getter private double lattitude,longitude;

    public Location(double lattitude,double longitude)
    {
        this.lattitude=lattitude;
        this.longitude=longitude;
        Hospital hospital = new Hospital("null",null);
    }

    public double getLattitude()
    {
        return lattitude;
    }
    public double getLongitude()
    {
        return longitude;
    }

    @Override
    public String toString()
    {
        return "{lattitude=" + lattitude +", longitude="+longitude+" }";
    }
}
