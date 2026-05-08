package com.sathish.thodar.features.journeyplanning;

import java.lang.System;

public class RouteView {
    
    public void displayRouteDetails(RouteModel route) {
        System.out.println("\n--- JOURNEY ROUTE DETAILS ---");
        System.out.println("From: " + route.getSourceStation());
        System.out.println("To: " + route.getDestinationStation());
        System.out.println("Via: " + route.getViaStations());
        System.out.println("-----------------------------");
    }
}