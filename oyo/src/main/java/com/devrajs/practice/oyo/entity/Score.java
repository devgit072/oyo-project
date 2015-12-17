package com.devrajs.practice.oyo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by devraj.singh on 11/17/15.
 */
public class Score {

    @Getter @Setter double combinedScore;
    @Getter @Setter double priceScore;
    @Getter @Setter double airportScore;
    @Getter @Setter double hospitalScore;
    public Score(double priceScore,double airportScore,double hospitalScore)
    {
        combinedScore=priceScore+airportScore+hospitalScore;
        this.priceScore=priceScore;
        this.hospitalScore=hospitalScore;
        this.airportScore=airportScore;
    }
}
