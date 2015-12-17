package com.devrajs.practice.oyo.entity;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by devraj.singh on 10/23/15.
 */


@lombok.Setter
@lombok.Getter
public class Hospital {

    private String name;
    private Location location;

    public Hospital(String name, Location location)
    {
        this.name=name;
        this.location=location;
    }

    public Hospital(String name,double lattitude,double longitude)
    {
        this.name=name;
        Location location = new Location(lattitude,longitude);
        this.location=location;
    }

    public String getName()
    {
        return name;
    }
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return "Name: "+name + " ,location: "+location.toString();
    }
}
