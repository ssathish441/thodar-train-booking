package com.sathish.thodar.features.support;

import java.lang.System;

public class SupportView {
    
    public void showTicketSubmissionSuccess(Long queryId) {
        System.out.println("\n[SUCCESS] Support ticket submitted!");
        System.out.println("Your Ticket ID is: " + queryId);
        System.out.println("Our admin will review and reply shortly.");
    }

    public void displaySupportTicket(SupportModel ticket) {
        System.out.println("\n--- SUPPORT TICKET [" + ticket.getQueryId() + "] ---");
        System.out.println("Issue: " + ticket.getIssueDescription());
        
        if (ticket.getIsResolved()) {
            System.out.println("Status: RESOLVED");
            System.out.println("Admin Reply: " + ticket.getAdminReply());
        } else {
            System.out.println("Status: PENDING");
            System.out.println("Admin Reply: Waiting for response...");
        }
        System.out.println("--------------------------------");
    }
}