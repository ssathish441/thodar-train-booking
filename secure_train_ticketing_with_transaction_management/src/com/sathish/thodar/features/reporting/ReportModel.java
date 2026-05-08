package com.sathish.thodar.features.reporting;

import java.lang.String;
import java.lang.Double;
import java.lang.Integer;

public class ReportModel {
    private String reportDate;
    private Integer totalTicketsSold;
    private Double totalRevenue;

    public ReportModel() {}

    public ReportModel(String reportDate, Integer totalTicketsSold, Double totalRevenue) {
        this.reportDate = reportDate;
        this.totalTicketsSold = totalTicketsSold;
        this.totalRevenue = totalRevenue;
    }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public Integer getTotalTicketsSold() { return totalTicketsSold; }
    public void setTotalTicketsSold(Integer totalTicketsSold) { this.totalTicketsSold = totalTicketsSold; }

    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
}