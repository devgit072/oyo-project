package com.devrajs.practice.oyo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by devraj.singh on 10/24/15.
 */
public class Place {

    @Getter @Setter private Location location;
    @Getter @Setter private String name;
    @Getter @Setter private String formattedAdress;

    public Place(String name, Location location, String formattedAdress)
    {
        this.name=name;
        this.location=location;
        this.formattedAdress=formattedAdress;
    }

    @Override
    public String toString()
    {
        String string=String.format(name+", "+location.toString()+", [%s]",formattedAdress);
        return string;
    }

}