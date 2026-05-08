package com.sathish.thodar.data.dto.request.admin;

import java.util.ArrayList;
import java.util.List;

public class TrainSetupRequest {
    private Long id; 
    private String trainNumber;
    private String trainName;
    private String sourceStation;
    private String destinationStation;
    private List<String> routeStations = new ArrayList<>();
    
    private int slCoaches;
    private int thirdAcCoaches;
    private int secondAcCoaches;
    private int firstAcCoaches;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrainNumber() { return trainNumber; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public String getSourceStation() { return sourceStation; }
    public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }
    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }
    
    public List<String> getRouteStations() { return routeStations; }
    public void setRouteStations(List<String> routeStations) { this.routeStations = routeStations; }

    public int getSlCoaches() { return slCoaches; }
    public void setSlCoaches(int slCoaches) { this.slCoaches = slCoaches; }
    public int getThirdAcCoaches() { return thirdAcCoaches; }
    public void setThirdAcCoaches(int thirdAcCoaches) { this.thirdAcCoaches = thirdAcCoaches; }
    public int getSecondAcCoaches() { return secondAcCoaches; }
    public void setSecondAcCoaches(int secondAcCoaches) { this.secondAcCoaches = secondAcCoaches; }
    public int getFirstAcCoaches() { return firstAcCoaches; }
    public void setFirstAcCoaches(int firstAcCoaches) { this.firstAcCoaches = firstAcCoaches; }
}