package com.sathish.thodar.features.admin;

import com.sathish.thodar.util.ConsoleInput;
import com.sathish.thodar.util.ParseHelper;
import com.sathish.thodar.data.repository.ThodarDB;
import com.sathish.thodar.data.dto.enums.TicketClass;
import com.sathish.thodar.data.dto.enums.TicketQuota;
import com.sathish.thodar.data.dto.enums.TicketStatus;
import com.sathish.thodar.data.dto.enums.ScheduleStatus;
import com.sathish.thodar.data.dto.request.admin.TrainSetupRequest;
import com.sathish.thodar.data.dto.request.admin.ScheduleRequest;
import com.sathish.thodar.data.dto.request.passenger.BookingRequest;
import com.sathish.thodar.features.support.SupportModel;
import com.sathish.thodar.features.support.SupportView;
import com.sathish.thodar.features.reporting.ReportModel;
import com.sathish.thodar.features.reporting.ReportView;
import com.sathish.thodar.service.TrainService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AdminView {

    private final ThodarDB db = ThodarDB.getInstance();
    private final SupportView supportView = new SupportView();
    private final ReportView reportView = new ReportView();
    private final Random random = new Random();

    private static final List<String> CHORD_LINE_MDU = Arrays.asList("MS/Chennai Egmore", "TBM/Tambaram", "CGL/Chengalpattu", "VM/Villupuram", "VRI/Vriddhachalam", "ALU/Ariyalur", "TPJ/Tiruchirappalli", "DG/Dindigul", "MDU/Madurai");
    private static final List<String> MAIN_LINE_MDU = Arrays.asList("MS/Chennai Egmore", "TBM/Tambaram", "CGL/Chengalpattu", "VM/Villupuram", "CUPJ/Cuddalore Port", "CDM/Chidambaram", "MV/Mayiladuthurai", "KMU/Kumbakonam", "TJ/Thanjavur", "TPJ/Tiruchirappalli", "DG/Dindigul", "MDU/Madurai");
    private static final List<String> EXT_TEN = Arrays.asList("VPT/Virudhunagar", "CVP/Kovilpatti", "TEN/Tirunelveli");
    private static final List<String> EXT_TN = Arrays.asList("VPT/Virudhunagar", "CVP/Kovilpatti", "MEJ/Vanchi Maniyachchi", "TN/Tuticorin"); 
    private static final List<String> EXT_SCT = Arrays.asList("VPT/Virudhunagar", "SVKS/Sivakasi", "RJPM/Rajapalayam", "TSI/Tenkasi", "SCT/Sengottai");
    private static final List<String> EXT_NCJ = Arrays.asList("VPT/Virudhunagar", "CVP/Kovilpatti", "TEN/Tirunelveli", "VLY/Valliyur", "NCJ/Nagercoil");

    public void showAdminMenu() {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("         ADMIN DASHBOARD         ");
            System.out.println("=================================");
            System.out.println("1. Add Train & Route");
            System.out.println("2. Schedule Train");
            System.out.println("3. View Passenger Chart");
            System.out.println("4. Answer Support Queries");
            System.out.println("5. Generate Revenue Report");
            System.out.println("6. Logout");
            String choice = ConsoleInput.getString("Choice: ").trim();

            switch (choice) {
                case "1": handleAddTrain(); break;
                case "2": handleAddSchedule(); break;
                case "3": handleViewChart(); break;
                case "4": handleAdminSupport(); break;
                case "5": handleAdminReport(); break;
                case "6": System.out.println("Admin logged out."); return;
                default: System.out.println("[ERROR] Invalid option.");
            }
        }
    }

    private void handleAddTrain() {
        System.out.println("\n--- ADD TRAIN ---");
        String tNum = ConsoleInput.getString("Train Number: ").trim(); 
        String tName = ConsoleInput.getString("Train Name: ").trim();
        
        System.out.println("\nSelect Line (Route Path):");
        System.out.println("1. Chord Line (via Ariyalur)");
        System.out.println("2. Main Line (via Thanjavur)");
        int lineChoice = ConsoleInput.getInt("Choice: ");
        List<String> baseRoute = (lineChoice == 1) ? new ArrayList<>(CHORD_LINE_MDU) : new ArrayList<>(MAIN_LINE_MDU);
        
        System.out.println("\n--- MAJOR STATIONS ---");
        System.out.println("1. MS - Chennai Egmore");
        System.out.println("2. TPJ - Tiruchirappalli");
        System.out.println("3. MDU - Madurai");
        System.out.println("4. TEN - Tirunelveli");
        System.out.println("5. TN - Tuticorin");
        System.out.println("6. SCT - Sengottai");
        System.out.println("7. NCJ - Nagercoil");

        int srcChoice = ConsoleInput.getInt("\n1. Select Source Station (1-7): ");
        int dstChoice = ConsoleInput.getInt("2. Select Destination Station (1-7): ");
        if (srcChoice == dstChoice) { System.out.println("[ERROR] Source and Destination cannot be the same!"); return; }

        int terminalChoice = Math.max(srcChoice, dstChoice); 
        List<String> masterRoute = new ArrayList<>(baseRoute);
        if (terminalChoice == 4) masterRoute.addAll(EXT_TEN); 
        else if (terminalChoice == 5) masterRoute.addAll(EXT_TN);
        else if (terminalChoice == 6) masterRoute.addAll(EXT_SCT); 
        else if (terminalChoice == 7) masterRoute.addAll(EXT_NCJ);

        String[] codes = {"", "MS", "TPJ", "MDU", "TEN", "TN", "SCT", "NCJ"};
        String srcCode = codes[srcChoice]; String dstCode = codes[dstChoice];

        int srcIdx = -1, dstIdx = -1;
        for (int i=0; i<masterRoute.size(); i++) {
            if (masterRoute.get(i).startsWith(srcCode)) srcIdx = i;
            if (masterRoute.get(i).startsWith(dstCode)) dstIdx = i;
        }

        if (srcIdx == -1 || dstIdx == -1) { System.out.println("[ERROR] Invalid Route Selection."); return; }

        List<String> finalRoute;
        if (srcIdx < dstIdx) { finalRoute = new ArrayList<>(masterRoute.subList(srcIdx, dstIdx + 1)); } 
        else { finalRoute = new ArrayList<>(masterRoute.subList(dstIdx, srcIdx + 1)); Collections.reverse(finalRoute); }

        System.out.println("\nEnter Coach Composition:");
        int sl = ConsoleInput.getInt("SL Coaches: "); int ac3 = ConsoleInput.getInt("3A Coaches: ");
        int ac2 = ConsoleInput.getInt("2A Coaches: "); int ac1 = ConsoleInput.getInt("1A Coaches: ");

        boolean isReturn = false;
        try { if (Integer.parseInt(tNum) % 2 == 0) isReturn = true; } catch (Exception e) {}

        TrainSetupRequest t1 = new TrainSetupRequest();
        t1.setTrainNumber(tNum); t1.setTrainName(tName);
        List<String> route1 = new ArrayList<>(finalRoute);
        if (isReturn) Collections.reverse(route1); 
        t1.setRouteStations(route1); t1.setSourceStation(route1.get(0)); t1.setDestinationStation(route1.get(route1.size() - 1));
        t1.setSlCoaches(sl); t1.setThirdAcCoaches(ac3); t1.setSecondAcCoaches(ac2); t1.setFirstAcCoaches(ac1);
        
        db.addTrain(t1); 
        System.out.println("\n[SUCCESS] Train " + tNum + " Added! (" + t1.getSourceStation() + " -> " + t1.getDestinationStation() + ")");

        try {
            int num = Integer.parseInt(tNum);
            String pairNum = (num % 2 == 0) ? String.valueOf(num - 1) : String.valueOf(num + 1);
            if (ConsoleInput.getString("Do you want to auto-generate the paired Return Train (" + pairNum + ")? (Y/N): ").equalsIgnoreCase("Y")) {
                TrainSetupRequest t2 = new TrainSetupRequest();
                t2.setTrainNumber(pairNum); t2.setTrainName(tName + " (Pair)");
                List<String> route2 = new ArrayList<>(finalRoute);
                Collections.reverse(route2); 
                t2.setRouteStations(route2); t2.setSourceStation(route2.get(0)); t2.setDestinationStation(route2.get(route2.size() - 1));
                t2.setSlCoaches(sl); t2.setThirdAcCoaches(ac3); t2.setSecondAcCoaches(ac2); t2.setFirstAcCoaches(ac1);
                db.addTrain(t2);
                System.out.println("[SUCCESS] Paired Train " + pairNum + " Added! (" + t2.getSourceStation() + " -> " + t2.getDestinationStation() + ")");
            }
        } catch (Exception e) { }
    }

    private void handleAddSchedule() {
        if (db.getAllTrains().isEmpty()) { System.out.println("\n[ERROR] No trains added yet!"); return; }
        System.out.println("\n--- AVAILABLE TRAINS ---");
        for (TrainSetupRequest t : db.getAllTrains()) System.out.println("ID: " + t.getId() + " | " + t.getTrainNumber() + " - " + t.getTrainName());
        
        Long trainId = ConsoleInput.getLong("Enter Train ID to Schedule: ");
        TrainSetupRequest train = db.getTrainById(trainId);
        if (train == null) { System.out.println("[ERROR] Invalid Train ID."); return; }

        ScheduleRequest s = new ScheduleRequest(); s.setTrainId(trainId);
        s.setJourneyDateEpoch(ParseHelper.dateToEpoch(ConsoleInput.getString("Date (dd-MM-yyyy): ")));
        String depTime = ConsoleInput.getString("Departure Time (HH:mm): ");
        String arrTime = ConsoleInput.getString("Arrival Time (HH:mm): ");
        System.out.println("[INFO] " + train.getTrainNumber() + " Scheduled: Departs at " + depTime + ", Arrives at " + arrTime);
        
        s.setStatus(ScheduleStatus.SCHEDULED); 
        ScheduleRequest saved = db.addSchedule(s);

        System.out.println("\n[INFO] Generating dummy bookings to simulate realistic availability...");
        for (TicketClass tc : TicketClass.values()) {
            int capacity = TrainService.getCnfCapacity(train, tc);
            if (capacity > 0) {
                double fillPercentage = 0.2 + (random.nextDouble() * 1.0); 
                bookDummySeats(saved.getId(), tc, train.getSourceStation(), (int)(capacity * fillPercentage));
                if (train.getRouteStations().size() > 6) {
                    String intermediate = train.getRouteStations().get(5);
                    double pqFillPercentage = 0.1 + (random.nextDouble() * 0.4); 
                    bookDummySeats(saved.getId(), tc, intermediate, (int)(capacity * pqFillPercentage));
                }
            }
        }
        System.out.println("[SUCCESS] Scheduled & Dummy Seats Booked!");
    }

    private void bookDummySeats(Long sId, TicketClass tClass, String station, int count) {
        int booked = 0;
        double price = (tClass == TicketClass.AC_1A) ? 2000 : (tClass == TicketClass.AC_2A) ? 1500 : (tClass == TicketClass.AC_3A) ? 1000 : 500;
        List<BookingRequest> dummyBookings = new ArrayList<>();
        String[] genders = {"M", "F"};
        
        while (booked < count) {
            int toBook = random.nextInt(4) + 1; 
            if (booked + toBook > count) toBook = count - booked;
            BookingRequest d = new BookingRequest(); 
            d.setUserId(999L); d.setScheduleId(sId); d.setTicketClass(tClass); d.setQuota(TicketQuota.GENERAL); 
            d.setBoardingStation(station); d.setPassengerCount(toBook); d.setStatus(TicketStatus.CNF); d.setTotalFare(price * toBook);
            for(int i=0; i<toBook; i++) {
                BookingRequest.PassengerDetail pd = new BookingRequest.PassengerDetail(); 
                pd.name = "Dummy_" + random.nextInt(100); pd.age = 20 + random.nextInt(40); pd.gender = genders[random.nextInt(2)];
                d.addPassenger(pd);
            }
            dummyBookings.add(d); booked += toBook;
        }
        Collections.shuffle(dummyBookings);
        for(BookingRequest b : dummyBookings) { db.addTicket(b); }
        TrainService.recalculateWaitlist(sId, tClass);
    }

    private void handleViewChart() {
        System.out.println("\n--- AVAILABLE SCHEDULES ---");
        List<ScheduleRequest> allSchedules = db.getAllSchedules();
        if (allSchedules.isEmpty()) { System.out.println("[ERROR] No schedules found!"); return; }
        for (ScheduleRequest s : allSchedules) {
            TrainSetupRequest t = db.getTrainById(s.getTrainId());
            System.out.println("ID: " + s.getId() + " | " + t.getTrainNumber() + " - " + t.getTrainName() + " | Date: " + ParseHelper.epochToDateString(s.getJourneyDateEpoch()));
        }
        Long sId = ConsoleInput.getLong("\nEnter Schedule ID: ");
        TrainSetupRequest train = db.getTrainById(db.getScheduleById(sId).getTrainId());
        
        for (TicketClass tc : TicketClass.values()) {
            if (TrainService.getCnfCapacity(train, tc) <= 0) continue; 
            System.out.println("\n============================================================");
            System.out.println("          PASSENGER CHART - " + tc);
            System.out.println("============================================================");
            System.out.println(String.format("%-10s | %-15s | %-4s | %-6s | %-10s", "Seat No", "Name", "Age", "Gender", "Status"));
            System.out.println("------------------------------------------------------------");
            boolean hasPax = false;
            for (BookingRequest t : db.getAllTickets()) {
                if (t.getScheduleId().equals(sId) && t.getTicketClass() == tc && t.getStatus() != TicketStatus.CAN) {
                    for (BookingRequest.PassengerDetail pd : t.getPassengers()) {
                        System.out.println(String.format("%-10s | %-15s | %-4d | %-6s | %-10s", pd.currentCoachSeat, pd.name, pd.age, pd.gender, pd.currentStatus));
                        hasPax = true;
                    }
                }
            }
            if (!hasPax) System.out.println("No passengers booked in " + tc);
            System.out.println("============================================================");
        }
    }

    private void handleAdminSupport() {
        System.out.println("\n--- USER SUPPORT TICKETS ---");
        boolean hasQueries = false;
        for(SupportModel ticket : db.getAllSupportTickets()) {
            hasQueries = true; supportView.displaySupportTicket(ticket);
            if(!ticket.getIsResolved()) {
                String reply = ConsoleInput.getString("Enter Reply (or press Enter to skip): ");
                if (!reply.trim().isEmpty()) { ticket.setAdminReply(reply); ticket.setIsResolved(true); System.out.println("[SUCCESS] Reply sent."); }
            }
        }
        if (!hasQueries) System.out.println("No pending support queries.");
    }

    private void handleAdminReport() {
        System.out.println("\n--- REVENUE REPORT ---");
        double rev = 0; int count = 0;
        for (BookingRequest t : db.getAllTickets()) {
            if(t.getStatus() != TicketStatus.CAN && t.getTotalFare() != null) { rev += t.getTotalFare(); count++; }
        }
        reportView.printDailySummary(new ReportModel(ParseHelper.epochToDateString(System.currentTimeMillis()), count, rev));
    }
}