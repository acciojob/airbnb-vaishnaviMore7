package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.sun.jdi.connect.Connector;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HotelManagementRepository {
    HashMap<String,Hotel>hotel_data=new HashMap<>();
    HashMap<String,Booking>Booking_data=new HashMap<>();
    HashMap<String,User>User_data=new HashMap<>();
    public String addHotel(Hotel hotel){ //error

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.

        if(hotel_data.containsValue(hotel)){ //error
            return "FAILURE";
        }
        else if (hotel.getHotelName()==null){
            return " ";

        } //error

        hotel_data.put(hotel.getHotelName(),hotel);
        return "SUCCESS";

    }

//error
    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        if(user.getaadharCardNo()==0){
            return null;
        }
        User_data.put(user.getName(), user); //error
        return user.getaadharCardNo();
    }
    HashMap<String,Integer>Hotel_Facility=new HashMap<>();
    private static Set<String> getKeysJava8(
            Map<String, Integer> map, Integer value) {

        return map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value)) //error
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }
    public String getHotelWithMostFacilities(){  //error

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        for(Map.Entry<String,Hotel>e:hotel_data.entrySet()){
            Hotel_Facility.put(e.getValue().getHotelName(),e.getValue().getFacilities().size());
        }
        int max= Collections.max(Hotel_Facility.values());
        String key=null;
        for (String key1 : getKeysJava8(Hotel_Facility, max)) { //error
            key=key1;
        }

        for(Map.Entry<String,Integer>e:Hotel_Facility.entrySet()){
            if(max==e.getValue()){
                if(key.length()<e.getKey().length()){  //error
                    return key;
                } else if (key.length()>e.getKey().length()) {
                    return e.getKey();
                }
            }
        }
        if(max==0){
            return " ";  //error
        }

        return key;
    }


    public int bookARoom(Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        int n=booking.getNoOfRooms();
        String name= booking.getHotelName();
        int total_amount=0;
        for(Map.Entry<String,Hotel>e:hotel_data.entrySet()){
            if(name.equals(e.getValue().getHotelName()) && n>e.getValue().getAvailableRooms()){
                return -1;
            }
            if(name.equals(e.getValue().getHotelName()) && n<e.getValue().getAvailableRooms()){
                total_amount=n*e.getValue().getPricePerNight();
            }

        }
        return total_amount;
    }

    int no_of_booking=0;
    public int getBookings(Integer aadharCard)
    {
        for(Map.Entry<String, Booking> e:Booking_data.entrySet()){
            if(e.getValue().getBookingAadharCard()==aadharCard){
                no_of_booking++;
            }
        }
        //In this function return the bookings done by a person

        return no_of_booking;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        Hotel hotel=null;

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        for(Map.Entry<String, Hotel> e:hotel_data.entrySet()){
            if(e.getValue().getFacilities().equals(newFacilities) && e.getValue().getHotelName().equals(hotelName)){
                break;
            }
            else{
                e.getValue().setFacilities(newFacilities);
                e.getValue().setHotelName(hotelName);
                hotel=e.getValue();

            }
        }

        return hotel ;
    }

}


