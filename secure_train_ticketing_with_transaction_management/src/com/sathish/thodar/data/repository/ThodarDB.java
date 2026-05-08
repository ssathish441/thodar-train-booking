package com.sathish.thodar.data.repository;

import com.sathish.thodar.data.dto.request.auth.RegisterRequest;
import com.sathish.thodar.data.dto.request.admin.TrainSetupRequest;
import com.sathish.thodar.data.dto.request.admin.ScheduleRequest;
import com.sathish.thodar.data.dto.request.passenger.BookingRequest;
import com.sathish.thodar.data.dto.response.passenger.Transaction;
import com.sathish.thodar.data.dto.enums.Role;
import com.sathish.thodar.data.dto.enums.TicketStatus;
import com.sathish.thodar.features.support.SupportModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ThodarDB {

    private static ThodarDB instance = null;

    private final List<RegisterRequest> users = new ArrayList<>();
    private final List<TrainSetupRequest> trains = new ArrayList<>();
    private final List<ScheduleRequest> schedules = new ArrayList<>();
    private final List<BookingRequest> tickets = new ArrayList<>();
    private final List<SupportModel> supportTickets = new ArrayList<>();

    private long userPk = 0L, trainPk = 0L, schedulePk = 0L, ticketPk = 0L, supportPk = 0L;

    private ThodarDB() {}

    public static ThodarDB getInstance() {
        if (instance == null) instance = new ThodarDB();
        return instance;
    }
    

    public List<ScheduleRequest> getAllSchedules() {
        return this.schedules; 
    }
    
    public List<BookingRequest> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    // USER OPERATIONS
    public RegisterRequest addUser(RegisterRequest user) {
        if (user == null || getUserByEmail(user.getEmail()) != null) return null;
        user.setId(++userPk);
        if (user.getRole() == null) user.setRole(Role.CUSTOMER);
        users.add(user);
        return user;
    }

    public RegisterRequest getUserByEmail(String email) {
        for (RegisterRequest u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public RegisterRequest authenticateUser(String email, String password) {
        RegisterRequest user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    // TRAIN & SCHEDULE OPERATIONS
    public TrainSetupRequest addTrain(TrainSetupRequest train) {
        train.setId(++trainPk);
        trains.add(train);
        return train;
    }

    public List<TrainSetupRequest> getAllTrains() { return new ArrayList<>(trains); }
    
    public TrainSetupRequest getTrainById(Long id) {
        for (TrainSetupRequest t : trains) if (t.getId().equals(id)) return t;
        return null;
    }

    public ScheduleRequest addSchedule(ScheduleRequest schedule) {
        schedule.setId(++schedulePk);
        schedules.add(schedule);
        return schedule;
    }

    public List<ScheduleRequest> getSchedulesForTrain(Long trainId) {
        List<ScheduleRequest> res = new ArrayList<>();
        for (ScheduleRequest s : schedules) if (s.getTrainId().equals(trainId)) res.add(s);
        return res;
    }

    public ScheduleRequest getScheduleById(Long id) {
        for (ScheduleRequest s : schedules) if (s.getId().equals(id)) return s;
        return null;
    }

    // TICKET OPERATIONS
    public BookingRequest addTicket(BookingRequest ticket) {
        ticket.setId(++ticketPk);
        ticket.setPnrNumber(String.format(Locale.ROOT, "PNR%06d", ticketPk));
        if (ticket.getStatus() == null) ticket.setStatus(TicketStatus.CNF);
        tickets.add(ticket);
        return ticket;
    }

    public BookingRequest getTicketByPnr(String pnr) {
        for (BookingRequest t : tickets) if (t.getPnrNumber().equalsIgnoreCase(pnr)) return t;
        return null;
    }

    public List<BookingRequest> getTicketsForUser(Long userId) {
        List<BookingRequest> res = new ArrayList<>();
        for (BookingRequest t : tickets) if (t.getUserId().equals(userId)) res.add(t);
        return res;
    }

    // SUPPORT FEATURE
    public SupportModel addSupportTicket(SupportModel ticket) {
        ticket.setQueryId(++supportPk);
        ticket.setIsResolved(false);
        supportTickets.add(ticket);
        return ticket;
    }
    
    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        List<Transaction> userTrans = new ArrayList<>();
        for(Transaction t : transactions) {
            if(t.userId.equals(userId)) userTrans.add(t);
        }
        return userTrans;
    }
    
    public List<SupportModel> getAllSupportTickets() { return new ArrayList<>(supportTickets); }
}