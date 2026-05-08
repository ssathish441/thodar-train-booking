package com.sathish.thodar.features.passenger;

import com.sathish.thodar.util.ConsoleInput;
import com.sathish.thodar.util.ParseHelper;
import com.sathish.thodar.data.repository.ThodarDB;
import com.sathish.thodar.data.dto.enums.TicketClass;
import com.sathish.thodar.data.dto.enums.TicketQuota;
import com.sathish.thodar.data.dto.enums.TicketStatus;
import com.sathish.thodar.data.dto.request.admin.TrainSetupRequest;
import com.sathish.thodar.data.dto.request.admin.ScheduleRequest;
import com.sathish.thodar.data.dto.request.auth.RegisterRequest;
import com.sathish.thodar.data.dto.request.passenger.BookingRequest;
import com.sathish.thodar.data.dto.response.auth.AuthResponse;
import com.sathish.thodar.data.dto.response.passenger.LiveStatusResponse;
import com.sathish.thodar.data.dto.response.passenger.TicketSummaryResponse;
import com.sathish.thodar.data.dto.response.passenger.Transaction;
import com.sathish.thodar.features.support.SupportModel;
import com.sathish.thodar.features.support.SupportView;
import com.sathish.thodar.service.TrainService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassengerView {

    private final ThodarDB db = ThodarDB.getInstance();
    private final AuthResponse loggedInUser;
    private final RegisterRequest loggedInUserEntity;
    private final SupportView supportView = new SupportView();
    private final Random random = new Random();

    public PassengerView(AuthResponse loggedInUser, RegisterRequest loggedInUserEntity) {
        this.loggedInUser = loggedInUser;
        this.loggedInUserEntity = loggedInUserEntity;
    }

    public void showPassengerMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("       PASSENGER DASHBOARD       ");
            System.out.println("=================================");
            System.out.println("Wallet Balance: Rs. " + loggedInUserEntity.getWalletBalance());
            System.out.println("---------------------------------");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. Transaction History");
            System.out.println("4. View My Tickets");
            System.out.println("5. Support Helpdesk");
            System.out.println("6. Simulate Dummy Cancellation");
            System.out.println("7. Recharge Wallet");
            System.out.println("8. Logout");
            String choice = ConsoleInput.getString("Choice: ").trim();

            switch (choice) {
                case "1": handleBookTicket(); break;
                case "2": handleCancelTicket(); break;
                case "3": viewTransactionHistory(); break;
                case "4": handleViewMyTickets(); break;
                case "5": handlePassengerSupport(); break;
                case "6": simulateDummyCancellation(); break;
                case "7": handleRechargeWallet(); break;
                case "8": System.out.println("Passenger logged out."); return;
                default: System.out.println("[ERROR] Invalid option.");
            }
        }
    }

    private void handleRechargeWallet() {
        double amt = ConsoleInput.getDouble("Enter amount to recharge (Rs): ");
        if (amt > 0) {
            loggedInUserEntity.setWalletBalance(loggedInUserEntity.getWalletBalance() + amt);
            db.addTransaction(new Transaction("WALLET", loggedInUser.getId(), amt, "CREDIT", "Recharge"));
            System.out.println("[SUCCESS] Recharged Rs. " + amt + ". New Balance: Rs. " + loggedInUserEntity.getWalletBalance());
        } else {
            System.out.println("[ERROR] Invalid amount.");
        }
    }

    private void handleBookTicket() {
        System.out.println("\n--- SEARCH TRAINS ---");
        String fromIn = ConsoleInput.getString("From Station (Code/Name): ").trim();
        String toIn = ConsoleInput.getString("To Station (Code/Name): ").trim();
        String dateIn = ConsoleInput.getString("Journey Date (dd-MM-yyyy): ").trim();
        
        System.out.println("\nSelect Quota: 1. GENERAL | 2. TATKAL");
        TicketQuota quota = (ConsoleInput.getInt("Choice: ") == 2) ? TicketQuota.TATKAL : TicketQuota.GENERAL;

        boolean found = false;
        List<LiveStatusResponse> results = new ArrayList<>();
        for (TrainSetupRequest t : db.getAllTrains()) {
            for (ScheduleRequest s : db.getSchedulesForTrain(t.getId())) {
                String schedDate = ParseHelper.epochToDateString(s.getJourneyDateEpoch());
                if (!schedDate.equals(dateIn)) continue; 
                
                if (quota == TicketQuota.TATKAL) {
                    long diff = s.getJourneyDateEpoch() - System.currentTimeMillis();
                    long oneDayMs = 24L * 60 * 60 * 1000;
                    if (diff > oneDayMs || diff < 0) continue; 
                }

                List<String> r = t.getRouteStations();
                int fIdx = -1, tIdx = -1; String fName = "", tName = "";
                for(int i=0; i<r.size(); i++) {
                    String[] parts = r.get(i).split("/"); String code = parts[0].trim(); String name = parts.length > 1 ? parts[1].trim() : code;
                    if(code.equalsIgnoreCase(fromIn) || name.equalsIgnoreCase(fromIn)) { fIdx = i; fName = code + "/" + name; }
                    if(code.equalsIgnoreCase(toIn) || name.equalsIgnoreCase(toIn)) { tIdx = i; tName = code + "/" + name; }
                }
                if(fIdx != -1 && tIdx != -1 && fIdx < tIdx) {
                    found = true; LiveStatusResponse res = new LiveStatusResponse();
                    res.scheduleId = s.getId(); res.trainInfo = t.getTrainNumber() + " - " + t.getTrainName(); res.route = fName + " to " + tName;
                    res.date = schedDate;
                    String boardFull = r.get(fIdx);
                    res.avail1A = TrainService.getAvail(s.getId(), TicketClass.AC_1A, boardFull, quota); 
                    res.avail2A = TrainService.getAvail(s.getId(), TicketClass.AC_2A, boardFull, quota);
                    res.avail3A = TrainService.getAvail(s.getId(), TicketClass.AC_3A, boardFull, quota); 
                    res.availSL = TrainService.getAvail(s.getId(), TicketClass.SL, boardFull, quota);
                    results.add(res);
                }
            }
        }
        
        if (!found) { 
            if (quota == TicketQuota.TATKAL) System.out.println("\n[ERROR] No trains available. Tatkal booking opens exactly 1 day before departure.");
            else System.out.println("\n[ERROR] No trains found for the selected Route & Date."); 
            return; 
        }

        for (LiveStatusResponse res : results) {
            System.out.println("\n======================================================================");
            System.out.println("[" + res.scheduleId + "] " + res.trainInfo + " | " + res.date);
            System.out.println("----------------------------------------------------------------------");
            System.out.println("  1A : " + String.format("%-15s", res.avail1A) + " |  2A : " + String.format("%-15s", res.avail2A));
            System.out.println("  3A : " + String.format("%-15s", res.avail3A) + " |  SL : " + String.format("%-15s", res.availSL));
            System.out.println("======================================================================");
        }

        Long sId = ConsoleInput.getLong("\nEnter Schedule ID to Book: ");
        int c = ConsoleInput.getInt("Select Class (1:1A, 2:2A, 3:3A, 4:SL): ");
        TicketClass tClass = (c==1)?TicketClass.AC_1A:(c==2)?TicketClass.AC_2A:(c==3)?TicketClass.AC_3A:TicketClass.SL;
        
        TrainSetupRequest selTrain = db.getTrainById(db.getScheduleById(sId).getTrainId());
        String fBoard = ""; String fDrop = "";
        for(String station : selTrain.getRouteStations()) {
            String[] parts = station.split("/");
            if(parts[0].equalsIgnoreCase(fromIn) || (parts.length > 1 && parts[1].equalsIgnoreCase(fromIn))) fBoard = station;
            if(parts[0].equalsIgnoreCase(toIn) || (parts.length > 1 && parts[1].equalsIgnoreCase(toIn))) fDrop = station;
        }

        if(TrainService.getAvail(sId, tClass, fBoard, quota).equals("REGRET")) { System.out.println("\n[ERROR] No seats available."); return; }

        int count = ConsoleInput.getInt("\nEnter Passengers Count (Max 6): ");
        
        List<BookingRequest.PassengerDetail> paxList = new ArrayList<>();
        System.out.println("\n--- PASSENGER DETAILS ---");
        for (int i=1; i<=count; i++) {
            BookingRequest.PassengerDetail pd = new BookingRequest.PassengerDetail();
            pd.name = ConsoleInput.getString("Passenger " + i + " Name: "); 
            pd.age = ConsoleInput.getInt("Age: "); 
            pd.gender = ConsoleInput.getString("Gender (M/F): ");
            paxList.add(pd);
        }

        double baseFare = ((c==1)?2000:(c==2)?1500:(c==3)?1000:500);
        double total = count * (quota == TicketQuota.TATKAL ? baseFare * 1.3 : baseFare); 

        System.out.println("\n--- PAYMENT PAGE ---");
        System.out.println("Total Fare: Rs. " + total);
        System.out.println("1. UPI");
        System.out.println("2. Wallet (Current Balance: Rs. " + loggedInUserEntity.getWalletBalance() + ")");
        System.out.println("3. Credit / Debit Card");
        
        int payChoice = ConsoleInput.getInt("Select Payment Method (1-3): ");

        if (payChoice == 2 && loggedInUserEntity.getWalletBalance() < total) {
            System.out.println("\n[ERROR] Insufficient Wallet Balance! Please recharge from Passenger Menu.");
            return;
        }

        System.out.println("\n[INFO] Connecting to Payment Gateway...");
        try {
            System.out.print("Processing");
            for(int i=0; i<4; i++) { Thread.sleep(700); System.out.print("."); }
            System.out.println();
        } catch (Exception e) {}

        double payAmt = ConsoleInput.getDouble("\nPlease enter the exact amount (Rs. " + total + ") to confirm booking: ");
        
        if (payAmt == total) {
            if (payChoice == 2) {
                loggedInUserEntity.setWalletBalance(loggedInUserEntity.getWalletBalance() - total);
            }
            
            BookingRequest b = new BookingRequest();
            b.setUserId(loggedInUser.getId()); b.setScheduleId(sId); b.setTicketClass(tClass); b.setQuota(quota);
            b.setBoardingStation(fBoard); b.setDropStation(fDrop); b.setPassengerCount(count);
            b.setPnrNumber("PNR" + (random.nextInt(90000) + 10000));
            
            for (BookingRequest.PassengerDetail pd : paxList) { b.addPassenger(pd); }

            b.setTotalFare(total); b.setStatus(TicketStatus.CNF);
            db.addTicket(b); TrainService.recalculateWaitlist(sId, tClass);
            
            String payMode = (payChoice == 1) ? "UPI" : (payChoice == 2) ? "WALLET" : "CARD";
            db.addTransaction(new Transaction(b.getPnrNumber(), loggedInUser.getId(), total, "DEBIT", "Booking via " + payMode));
            
            System.out.println("\n[SUCCESS] Payment Received! Ticket Booked via " + payMode + ".");
            if (payChoice == 2) System.out.println("Remaining Wallet Balance: Rs. " + loggedInUserEntity.getWalletBalance());
            
            printTicket(db.getTicketByPnr(b.getPnrNumber()));
        } else {
            System.out.println("\n[ERROR] Incorrect amount entered. Booking Cancelled!");
        }
    }

    private void handleCancelTicket() {
        System.out.println("\n--- CANCEL TICKET ---");
        String pnr = ConsoleInput.getString("Enter PNR Number to cancel: ");
        BookingRequest t = db.getTicketByPnr(pnr);
        if (t != null && t.getStatus() != TicketStatus.CAN) {
            double refund = t.getTotalFare() * 0.80;
            System.out.println("Refund Amount: Rs. " + refund + " (20% Fee applied)");
            if (ConsoleInput.getString("Confirm cancellation? (Y/N): ").equalsIgnoreCase("Y")) {
                t.setStatus(TicketStatus.CAN); 
                loggedInUserEntity.setWalletBalance(loggedInUserEntity.getWalletBalance() + refund);
                db.addTransaction(new Transaction(pnr, loggedInUser.getId(), refund, "CREDIT", "Ticket Refund"));
                TrainService.recalculateWaitlist(t.getScheduleId(), t.getTicketClass()); 
                System.out.println("[SUCCESS] Ticket Cancelled & Refunded to Wallet.");
            }
        } else {
            System.out.println("[ERROR] Invalid PNR or Ticket already cancelled.");
        }
    }

    private void viewTransactionHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        List<Transaction> txns = db.getTransactionsByUserId(loggedInUser.getId());
        if(txns.isEmpty()) { System.out.println("No transactions recorded."); return; }

        System.out.println(String.format("%-12s | %-10s | %-8s | %-8s | %-15s", "TXN ID", "PNR", "AMT", "TYPE", "REMARK"));
        System.out.println("------------------------------------------------------------------");
        for(Transaction txn : txns) {
            System.out.println(String.format("%-12s | %-10s | %-8.2f | %-8s | %-15s", txn.tId, txn.pnr, txn.amount, txn.type, txn.remark));
        }
    }

    private void simulateDummyCancellation() {
        System.out.println("\n[SIMULATING TIME PASSING...]");
        int cancelledSeats = 0;
        for (BookingRequest t : db.getAllTickets()) {
            if (t.getUserId() == 999L && t.getStatus() != TicketStatus.CAN && random.nextInt(100) < 15) {
                t.setStatus(TicketStatus.CAN); TrainService.recalculateWaitlist(t.getScheduleId(), t.getTicketClass());
                cancelledSeats += t.getPassengerCount();
            }
        }
        if (cancelledSeats > 0) System.out.println("[SUCCESS] " + cancelledSeats + " dummy passengers cancelled their tickets! Check 'View My Tickets' to see if your RAC/WL upgraded!");
        else System.out.println("[INFO] No one cancelled their tickets right now. Try again later.");
    }

    private void handleViewMyTickets() {
        System.out.println("\n--- MY TICKETS ---");
        List<BookingRequest> myTickets = db.getTicketsForUser(loggedInUser.getId());
        if (myTickets.isEmpty()) { System.out.println("No tickets booked."); return; }
        for (BookingRequest t : myTickets) printTicket(t);
    }

    private void printTicket(BookingRequest t) {
        ScheduleRequest s = db.getScheduleById(t.getScheduleId());
        TrainSetupRequest train = db.getTrainById(s.getTrainId());
        
        TicketSummaryResponse res = new TicketSummaryResponse();
        res.pnrNumber = t.getPnrNumber();
        res.trainDetails = train.getTrainNumber() + " - " + train.getTrainName();
        res.routeDetails = t.getBoardingStation() + " TO " + t.getDropStation();
        res.journeyDate = ParseHelper.epochToDateString(s.getJourneyDateEpoch());
        res.ticketClass = t.getTicketClass().toString();
        res.mainStatus = (t.getStatus() == TicketStatus.CAN) ? "CANCELLED" : "BOOKED";

        for (BookingRequest.PassengerDetail pd : t.getPassengers()) {
            TicketSummaryResponse.PassengerResponse pr = new TicketSummaryResponse.PassengerResponse();
            pr.name = pd.name; pr.age = pd.age; pr.gender = pd.gender;
            pr.bookingStatusInfo = pd.bookingStatus + " (" + pd.bookingCoachSeat + ")";
            pr.currentStatusInfo = pd.currentStatus + " (" + pd.currentCoachSeat + ")";
            res.passengers.add(pr);
        }

        System.out.println("\n==================================================================================================");
        System.out.println("PNR: " + res.pnrNumber + " | Train: " + res.trainDetails + " | Quota: " + t.getQuota());
        System.out.println("Route: " + res.routeDetails + " | Date: " + res.journeyDate);
        System.out.println("Class: " + res.ticketClass + " | Ticket Status: " + res.mainStatus);
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println(String.format("%-15s | %-3s | %-6s | %-22s | %-22s", "Name", "Age", "Gender", "Booking Status", "Current Status"));
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (TicketSummaryResponse.PassengerResponse pr : res.passengers) {
            System.out.println(String.format("%-15s | %-3d | %-6s | %-22s | %-22s", pr.name, pr.age, pr.gender, pr.bookingStatusInfo, pr.currentStatusInfo));
        }
        System.out.println("==================================================================================================");
    }

    private void handlePassengerSupport() {
        System.out.println("\n--- SUPPORT HELPDESK ---");
        System.out.println("1. Raise new issue");
        System.out.println("2. View my issues");
        if (ConsoleInput.getInt("Choice: ") == 1) {
            SupportModel query = new SupportModel(); query.setUserId(loggedInUser.getId());
            query.setIssueDescription(ConsoleInput.getString("Describe your issue: "));
            db.addSupportTicket(query); System.out.println("\n[SUCCESS] Ticket raised! Admin will reply soon.");
        } else {
            boolean hasIssues = false;
            for(SupportModel t : db.getAllSupportTickets()) {
                if(t.getUserId().equals(loggedInUser.getId())) { supportView.displaySupportTicket(t); hasIssues = true; }
            }
            if(!hasIssues) System.out.println("\nYou have no support tickets.");
        }
    }
}