package com.sathish.thodar.data.dto.request.passenger;

import com.sathish.thodar.data.dto.enums.TicketClass;
import com.sathish.thodar.data.dto.enums.TicketStatus;
import com.sathish.thodar.data.dto.enums.TicketQuota; // PUTHUSA ADD PANNATHU
import java.util.ArrayList;
import java.util.List;

public class BookingRequest {
    private Long id;
    private String pnrNumber; 
    private Long userId;
    private Long scheduleId;
    
    private String boardingStation;
    private String dropStation;
    
    private TicketClass ticketClass;
    private TicketQuota quota; // PUTHUSA ADD PANNATHU
    
    private Integer passengerCount;
    private Double totalFare;
    private TicketStatus status;
    
    public static class PassengerDetail {
        public String name;
        public int age;
        public String gender;
        public String bookingCoachSeat;
        public String bookingStatus;
        public String currentCoachSeat;
        public String currentStatus;
    }

    private List<PassengerDetail> passengers = new ArrayList<>();

    public BookingRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPnrNumber() { return pnrNumber; }
    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    
    public String getBoardingStation() { return boardingStation; }
    public void setBoardingStation(String boardingStation) { this.boardingStation = boardingStation; }
    public String getDropStation() { return dropStation; }
    public void setDropStation(String dropStation) { this.dropStation = dropStation; }

    public TicketClass getTicketClass() { return ticketClass; }
    public void setTicketClass(TicketClass ticketClass) { this.ticketClass = ticketClass; }
    
    // QUOTA GETTER AND SETTER
    public TicketQuota getQuota() { return quota; }
    public void setQuota(TicketQuota quota) { this.quota = quota; }
    
    public Integer getPassengerCount() { return passengerCount; }
    public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }
    public Double getTotalFare() { return totalFare; }
    public void setTotalFare(Double totalFare) { this.totalFare = totalFare; }
    public TicketStatus getStatus() { return status; }
    public void setStatus(TicketStatus status) { this.status = status; }
    
    public List<PassengerDetail> getPassengers() { return passengers; }
    public void addPassenger(PassengerDetail detail) { this.passengers.add(detail); }
}