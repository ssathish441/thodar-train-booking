package com.sathish.thodar.data.dto.response.passenger;

import java.util.ArrayList;
import java.util.List;

public class TicketSummaryResponse {
    public String pnrNumber;
    public String trainDetails;
    public String routeDetails;
    public String journeyDate;
    public String ticketClass;
    public String mainStatus;

    public static class PassengerResponse {
        public String name;
        public int age;
        public String gender;
        public String bookingStatusInfo;
        public String currentStatusInfo;
    }
    
    public List<PassengerResponse> passengers = new ArrayList<>();
}