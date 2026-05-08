package com.sathish.thodar.features.journeyplanning;

import java.lang.String;

public class RouteModel {
    private String sourceStation;
    private String destinationStation;
    private String viaStations;

    public RouteModel() {}

    public RouteModel(String sourceStation, String destinationStation, String viaStations) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.viaStations = viaStations;
    }

    public String getSourceStation() { return sourceStation; }
    public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }

    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }

    public String getViaStations() { return viaStations; }
    public void setViaStations(String viaStations) { this.viaStations = viaStations; }
}