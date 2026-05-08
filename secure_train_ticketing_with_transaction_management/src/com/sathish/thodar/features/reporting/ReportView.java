package com.sathish.thodar.features.reporting;

import java.lang.System;

public class ReportView {
    
    public void printDailySummary(ReportModel report) {
        System.out.println("\n========== DAILY COLLECTION REPORT ==========");
        System.out.println("Date           : " + report.getReportDate());
        System.out.println("Tickets Sold   : " + report.getTotalTicketsSold());
        System.out.println("Total Revenue  : Rs. " + report.getTotalRevenue());
        System.out.println("=============================================");
    }
}