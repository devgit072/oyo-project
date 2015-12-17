package com.devrajs.practice.oyo;

import com.devrajs.practice.oyo.entity.*;
import com.devrajs.practice.oyo.exception.HttpResponseFailedException;
import com.devrajs.practice.oyo.exception.InvalidResponseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by devraj.singh on 9/26/15.
 */
public class HotelGetter {

        private String templateApi="http://utilities-oyorooms.herokuapp.com/api/v2/search/hotels?filters[coordinates][longitude]=$longitutude&filters[coordinates][latitude]=$lattitude&filters[coordinates][distance]=$radius&fields=name,longitude,latitude,oyo_id&access_token=$oyo_acces_token&additional_fields=room_pricing";
        final int radius = 40; //in KM

        Logger logger = LoggerFactory.getLogger(HotelGetter.class);

        public String getHotelListInJson(double lattitude,double longitude, String sortingCriteria) throws InvalidResponseException, HttpResponseFailedException {
            Location location = new Location(lattitude,longitude);
            List<Hotel> list = findHotelListForGivenLocation(location,radius);
            SortingCriteria sortingCriteria1 = getSortingCriteria(sortingCriteria);
            String jsonResult = ResultFormatter.formJsonResponse(list,sortingCriteria1,radius);
            return jsonResult;
        }
        List<Hotel> findHotelListForGivenLocation(Location location, int radius) throws HttpResponseFailedException, InvalidResponseException {
                String api = templateApi;
                api = api.replace("$longitutude",String.valueOf(location.getLongitude()));
                api = api.replace("$lattitude",String.valueOf(location.getLattitude()));
                api = api.replace("$radius",radius+"");
                api = api.replace("$oyo_acces_token", KeyProvider.getOyoAccessToken());
            logger.debug("Api to be hit: {}",api);
                CustomResponse response = HttpHelper.httpget(api);
                if(response==null)
                        throw  new HttpResponseFailedException("Response returned is null...plz check url");
                if(response.getResponseCode()!=200) {
                    logger.error("Response code: "+response.getResponseCode());
                    throw new HttpResponseFailedException("response code is NOT 200, status code=" + response.getResponseCode());
                }
                String jsonResponse= response.getContent();
                List<Hotel> hotelList = processJsonResponse(jsonResponse);
                return hotelList;
        }

        private List<Hotel> processJsonResponse(String json) throws InvalidResponseException, HttpResponseFailedException {
                List<Hotel> hotelList = new ArrayList<>();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(json);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int count=jsonObject.get("count").getAsInt();
                if(count==0) {
                        System.out.println("No result found for given location!!");
                        return hotelList;
                }
                JsonArray jsonArray = jsonObject.get("hotels").getAsJsonArray();
                Iterator<JsonElement> iterator= jsonArray.iterator();
                while (iterator.hasNext())
                {
                        JsonObject hotelObject = iterator.next().getAsJsonObject();
                        String name = hotelObject.get("name").getAsString();
                        double lattitude=hotelObject.get("latitude").getAsDouble();
                        double longitude=hotelObject.get("longitude").getAsDouble();
                        String oyoId=hotelObject.get("oyo_id").getAsString();
                        JsonArray priceArray = hotelObject.getAsJsonArray("pricing");
                        Price price = new Price(priceArray.get(0).getAsInt(),priceArray.get(1).getAsInt(),priceArray.get(2).getAsInt());
                        Hotel hotel = new Hotel(name,oyoId,new Location(lattitude,longitude),price);
                        hotelList.add(hotel);
                }
                return hotelList;
        }

    List<Hotel> sortHotelBy(List<Hotel> hotelList, SortingCriteria sortingCriteria)
    {
        Hotel[] hotels = new Hotel[hotelList.size()];
        hotelList.toArray(hotels);
        int n = hotels.length;
        for(int i=0;i<n-1;i++)
        {
            for(int j=i+1;j<n;j++)
            {
                double score1;
                double score2;
                if(sortingCriteria == SortingCriteria.CombinedScore)
                {
                    score1 = hotels[i].getScore().getCombinedScore();
                    score2 = hotels[j].getScore().getCombinedScore();
                }
                else if(sortingCriteria == SortingCriteria.HotelPriceScore)
                {
                    score1 = hotels[i].getScore().getPriceScore();
                    score2 = hotels[j].getScore().getPriceScore();
                }
                else if(sortingCriteria == SortingCriteria.AirportScore)
                {
                    score1 = hotels[i].getScore().getAirportScore();
                    score2 = hotels[j].getScore().getAirportScore();
                }
                else
                {
                    score1 = hotels[i].getScore().getHospitalScore();
                    score2 = hotels[j].getScore().getHospitalScore();
                }
                if(score1<score2)
                {
                    Hotel temp = hotels[i];
                    hotels[i]=hotels[j];
                    hotels[j]=temp;
                }
            }
        }
        List<Hotel> list = new LinkedList<>();
        for(Hotel hotel : hotels)
        {
            list.add(hotel);
        }
        return list;
    }

    List<Hotel> getHotelListBy(Location location, SortingCriteria sortingCriteria) throws InvalidResponseException, HttpResponseFailedException {
        List<Hotel> initialList = findHotelListForGivenLocation(location,radius);
        List<Hotel> sortedList=sortHotelBy(initialList,sortingCriteria);
        return sortedList;
    }

    SortingCriteria getSortingCriteria(String sortingCriteria)
    {
        String key = sortingCriteria.toLowerCase();
        if(key.contains("combined") || key.contains("all") || key.contains("combine"))
            return SortingCriteria.CombinedScore;
        else if(key.contains("hotel"))
            return SortingCriteria.HotelPriceScore;
        else if(key.contains("hospital"))
            return SortingCriteria.HospitalScore;
        else if(key.contains("airport"))
            return SortingCriteria.AirportScore;
        else
            return SortingCriteria.CombinedScore;
    }

        public static void main(String[] args) throws InvalidResponseException, HttpResponseFailedException {
                HotelGetter hotelGetter = new HotelGetter();
                Location l = new Location(12.9667,77.5667);
                List<Hotel> list = hotelGetter.findHotelListForGivenLocation(l, 20);
            List<Hotel> a = hotelGetter.sortHotelBy(list,SortingCriteria.CombinedScore);
            List<Hotel> b = hotelGetter.sortHotelBy(list,SortingCriteria.HotelPriceScore);
            List<Hotel> c = hotelGetter.sortHotelBy(list,SortingCriteria.AirportScore);
            List<Hotel> d = hotelGetter.sortHotelBy(list,SortingCriteria.HospitalScore);
                for(Hotel hotel : a)
                {
                        System.out.println(hotel.toString());
                }
            System.out.println("##################");
            System.out.println("##################");
            for(Hotel hotel : b)
            {
                System.out.println(hotel.toString());
            }
            System.out.println("##################");
            System.out.println("##################");
            for(Hotel hotel : c)
            {
                System.out.println(hotel.toString());
            }
            System.out.println("##################");
            System.out.println("##################");
            for(Hotel hotel : d)
            {
                System.out.println(hotel.toString());
            }
            System.out.println("#################");
            System.out.println("#################");
            System.out.println("#################");
            System.out.println("#################");
            ResultFormatter resultFormatter = new ResultFormatter();
            String result = resultFormatter.formJsonResponse(a,SortingCriteria.CombinedScore,hotelGetter.radius);
            System.out.println(result);
        }
}
