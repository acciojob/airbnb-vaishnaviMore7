package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.sun.jdi.connect.Connector;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HotelManagementRepository {
    HashMap<String,Hotel>hotel_data=new HashMap<>();
    HashMap<String,Booking>Booking_data=new HashMap<>();
    HashMap<Integer,User>User_data=new HashMap<>();
    HashMap<Integer,Integer> countOfBookings=new HashMap<>();
    public String addHotel(Hotel hotel){ //error

        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.

        if (hotel==null || hotel.getHotelName()==null){
            return "FAILURE ";
        } //error
        if(hotel_data.containsKey(hotel.getHotelName())){ //error
            return "FAILURE";
        }
        hotel_data.put(hotel.getHotelName(),hotel);
        return "SUCCESS";

    }

//error
    public Integer addUser(User user){

        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user

        User_data.put(user.getaadharCardNo(), user);
         //error
        return user.getaadharCardNo();
    }
    HashMap<String,Integer>Hotel_Facility=new HashMap<>();
    public String getHotelWithMostFacilities(){  //error

        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        /*for(Map.Entry<String,Hotel>e:hotel_data.entrySet()){
            Hotel_Facility.put(e.getValue().getHotelName(),e.getValue().getFacilities().size());
        }
        int max= Collections.max(Hotel_Facility.values());
        String ans=null;
        for (Map.Entry<String,Integer>e:Hotel_Facility.entrySet()) { //error
            if(e.getValue()==max){
                ans=e.getKey();
            }
        }

        for(Map.Entry<String,Integer>e:Hotel_Facility.entrySet()){
            if(max==e.getValue()){
                if(ans.length()<e.getKey().length()){  //error
                    return ans;
                } else if (ans.length()>e.getKey().length()) {
                    return e.getKey();
                }
            }
            if(max==0){
                return "";
            }
        }


        return ans;*/
        int facilities= 0;

        String hotelName = "";

        for(Hotel hotel:hotel_data.values()){

            if(hotel.getFacilities().size()>facilities){
                facilities = hotel.getFacilities().size();
                hotelName = hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facilities){
                if(hotel.getHotelName().compareTo(hotelName)<0){
                    hotelName = hotel.getHotelName();
                }
            }
        }
        return hotelName;
    }



    public int bookARoom(@NotNull Booking booking){

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid
        String key = UUID.randomUUID().toString();

        booking.setBookingId(key);

        String hotelName = booking.getHotelName();

        Hotel hotel = hotel_data.get(hotelName);

        int availableRooms = hotel.getAvailableRooms();

        if(availableRooms<booking.getNoOfRooms()){
            return -1;
        }

        int amountToBePaid = hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);

        //Make sure we check this part of code as well
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());

        Booking_data.put(key,booking);

        hotel_data.put(hotelName,hotel);

        int aadharCard = booking.getBookingAadharCard();
        Integer currentBookings = countOfBookings.get(aadharCard);
        countOfBookings.put(aadharCard, Objects.nonNull(currentBookings)?1+currentBookings:1);
        return amountToBePaid;
    }
    public int getBookings(Integer aadharCard)
    {

        //In this function return the bookings done by a person

        return countOfBookings.get(aadharCard);
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible
        /*for(Map.Entry<String, Hotel> e:hotel_data.entrySet()){
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
    }*/
        List<Facility> oldFacilities = hotel_data.get(hotelName).getFacilities();

        for(Facility facility: newFacilities){

            if(oldFacilities.contains(facility)){
                continue;
            }else{
                oldFacilities.add(facility);
            }
        }

        Hotel hotel = hotel_data.get(hotelName);
        hotel.setFacilities(oldFacilities);

        hotel_data.put(hotelName,hotel);

        return hotel;
    }

}


