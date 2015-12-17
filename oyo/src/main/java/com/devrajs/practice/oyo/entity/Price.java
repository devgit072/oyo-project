package com.devrajs.practice.oyo.entity;

/**
 * Created by devraj.singh on 10/24/15.
 */
public class Price {

    private int singleBedPrice;
    private int doubleBedPrice;
    private int halfDayExtraPrice;
    private int averagePrice;

    public Price(int singleBedPrice,int doubleBedPrice, int halfDayExtraPrice)
    {
        this.singleBedPrice=singleBedPrice;
        this.doubleBedPrice=doubleBedPrice;
        this.halfDayExtraPrice=halfDayExtraPrice;
        averagePrice = (singleBedPrice+doubleBedPrice+halfDayExtraPrice)/3;
    }

    public int getSingleBedPrice()
    {
        return singleBedPrice;
    }
    public int getDoubleBedPrice()
    {
        return doubleBedPrice;
    }
    public int getHalfDayExtraPrice()
    {
        return halfDayExtraPrice;
    }
    public int getAveragePrice()
    {
        return averagePrice;
    }
}
