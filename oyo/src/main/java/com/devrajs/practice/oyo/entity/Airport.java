package com.devrajs.practice.oyo.entity;

/**
 * Created by devraj.singh on 10/24/15.
 */
public class Airport {

    private String name;
    private Location location;
    private String vicinity;

    public Airport(String name, Location location,String vicinity)
    {
        this.name=name;
        this.location=location;
        this.vicinity=vicinity;
    }

    public Airport(String name, double lattitude,double longitude,String vicinity)
    {
        this.name = name;
        this.location=new Location(lattitude,longitude);
        this.vicinity=vicinity;
    }

    public String getName()
    {
        return name;
    }
    public Location getLocation()
    {
        return location;
    }
    public String getVicinity()
    {
        return vicinity;
    }

    @Override
    public String toString()
    {
        return String.format("Name: %s, Vicinity: %s, Loaction: {%s}",name,vicinity,location.toString());
    }

}
