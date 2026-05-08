package com.sathish.thodar.data.dto.request.admin;

import com.sathish.thodar.data.dto.enums.ScheduleStatus;

public class ScheduleRequest {
    private Long id; 
    private Long trainId;
    private Long journeyDateEpoch;
    private Long departureTimeEpoch;
    private Long arrivalTimeEpoch;
    private ScheduleStatus status; // <-- Status property

    public ScheduleRequest() {}

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public Long getJourneyDateEpoch() { return journeyDateEpoch; }
    public void setJourneyDateEpoch(Long journeyDateEpoch) { this.journeyDateEpoch = journeyDateEpoch; }

    public Long getDepartureTimeEpoch() { return departureTimeEpoch; }
    public void setDepartureTimeEpoch(Long departureTimeEpoch) { this.departureTimeEpoch = departureTimeEpoch; }

    public Long getArrivalTimeEpoch() { return arrivalTimeEpoch; }
    public void setArrivalTimeEpoch(Long arrivalTimeEpoch) { this.arrivalTimeEpoch = arrivalTimeEpoch; }

    public ScheduleStatus getStatus() { return status; }
    public void setStatus(ScheduleStatus status) { this.status = status; }
}