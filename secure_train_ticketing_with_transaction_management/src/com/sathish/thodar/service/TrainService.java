package com.sathish.thodar.service;

import com.sathish.thodar.data.repository.ThodarDB;
import com.sathish.thodar.data.dto.enums.TicketClass;
import com.sathish.thodar.data.dto.enums.TicketQuota;
import com.sathish.thodar.data.dto.enums.TicketStatus;
import com.sathish.thodar.data.dto.request.admin.TrainSetupRequest;
import com.sathish.thodar.data.dto.request.passenger.BookingRequest;

public class TrainService {

    private static final ThodarDB db = ThodarDB.getInstance();

    public static final int SEATS_SL = 70;
    public static final int SEATS_3A = 64;
    public static final int SEATS_2A = 54;
    public static final int SEATS_1A = 24;
    public static final int RAC_BERTHS_SL = 7;
    public static final int RAC_BERTHS_3A = 4;
    public static final int RAC_BERTHS_2A = 3;
    public static final int MAX_WL = 100;

    public static int getCnfCapacity(TrainSetupRequest tr, TicketClass tc) {
        if (tc == TicketClass.SL) return tr.getSlCoaches() * SEATS_SL;
        if (tc == TicketClass.AC_3A) return tr.getThirdAcCoaches() * SEATS_3A;
        if (tc == TicketClass.AC_2A) return tr.getSecondAcCoaches() * SEATS_2A;
        if (tc == TicketClass.AC_1A) return tr.getFirstAcCoaches() * SEATS_1A;
        return 0;
    }

    public static int getRacCapacity(TrainSetupRequest tr, TicketClass tc) {
        if (tc == TicketClass.SL) return tr.getSlCoaches() * RAC_BERTHS_SL * 2;
        if (tc == TicketClass.AC_3A) return tr.getThirdAcCoaches() * RAC_BERTHS_3A * 2;
        if (tc == TicketClass.AC_2A) return tr.getSecondAcCoaches() * RAC_BERTHS_2A * 2;
        return 0;
    }

    public static String getWaitlistType(TrainSetupRequest train, String boardingStation, TicketQuota quota) {
        if (quota == TicketQuota.TATKAL) return "TQWL";
        int bIdx = -1;
        for (int i=0; i<train.getRouteStations().size(); i++) {
            if (train.getRouteStations().get(i).equalsIgnoreCase(boardingStation)) { bIdx = i; break; }
        }
        if (bIdx >= 0 && bIdx <= 2) return "GNWL";
        return "PQWL";
    }

    public static String getAvail(Long sId, TicketClass tc, String boardingStation, TicketQuota quota) {
        TrainSetupRequest tr = db.getTrainById(db.getScheduleById(sId).getTrainId());
        String wlType = getWaitlistType(tr, boardingStation, quota);
        int totalCnf = getCnfCapacity(tr, tc);
        
        int coaches = (tc == TicketClass.SL) ? tr.getSlCoaches() : (tc == TicketClass.AC_3A) ? tr.getThirdAcCoaches() : (tc == TicketClass.AC_2A) ? tr.getSecondAcCoaches() : tr.getFirstAcCoaches();
        int tqC = coaches * 12; 
        int pqC = totalCnf / 5; 
        int gnC = totalCnf - tqC - pqC; 
        
        int poolCnf = (wlType.equals("GNWL")) ? gnC : (wlType.equals("PQWL")) ? pqC : tqC;
        int poolRac = (wlType.equals("GNWL")) ? getRacCapacity(tr, tc) : 0; 
        
        int bookedInPool = 0;
        for (BookingRequest t : db.getAllTickets()) {
            if (t.getScheduleId().equals(sId) && t.getTicketClass() == tc && t.getStatus() != TicketStatus.CAN) {
                if (getWaitlistType(tr, t.getBoardingStation(), t.getQuota()).equals(wlType)) bookedInPool += t.getPassengerCount();
            }
        }
        
        if (poolCnf - bookedInPool > 0) return "AVL " + (poolCnf - bookedInPool);
        int over = bookedInPool - poolCnf;
        if (over < poolRac) return "RAC " + (over + 1);
        if (over < poolRac + MAX_WL) return wlType + " " + (over - poolRac + 1);
        return "REGRET";
    }

    public static void recalculateWaitlist(Long sId, TicketClass tc) {
        TrainSetupRequest tr = db.getTrainById(db.getScheduleById(sId).getTrainId());
        int totalCnf = getCnfCapacity(tr, tc);
        int coaches = (tc == TicketClass.SL) ? tr.getSlCoaches() : (tc == TicketClass.AC_3A) ? tr.getThirdAcCoaches() : (tc == TicketClass.AC_2A) ? tr.getSecondAcCoaches() : tr.getFirstAcCoaches();
        
        String p = (tc == TicketClass.AC_1A) ? "H" : (tc == TicketClass.AC_2A) ? "A" : (tc == TicketClass.AC_3A) ? "B" : "S";
        int seatsPerCoach = (p.equals("S")) ? SEATS_SL : (p.equals("B")) ? SEATS_3A : (p.equals("A")) ? SEATS_2A : SEATS_1A;
        
        int tqC = coaches * 12;
        int pqC = totalCnf / 5;
        int gnC = totalCnf - tqC - pqC;
        int gnR = getRacCapacity(tr, tc); 
        
        int gnP = 0, pqP = 0, tqP = 0;

        for (BookingRequest r : db.getAllTickets()) {
            if (r.getScheduleId().equals(sId) && r.getTicketClass() == tc && r.getStatus() != TicketStatus.CAN) {
                String wl = getWaitlistType(tr, r.getBoardingStation(), r.getQuota());
                
                int currentPos = (wl.equals("GNWL")) ? gnP : (wl.equals("PQWL")) ? pqP : tqP;
                int cnfCap = (wl.equals("GNWL")) ? gnC : (wl.equals("PQWL")) ? pqC : tqC;

                if (currentPos < cnfCap) {
                    int cnfOffset = (wl.equals("GNWL")) ? 0 : (wl.equals("PQWL")) ? gnC : gnC + pqC;
                    int physicalSeat = currentPos + cnfOffset;
                    int remainingInCoach = seatsPerCoach - (physicalSeat % seatsPerCoach);
                    int passCount = r.getPassengerCount();

                    if (passCount > 1 && passCount <= seatsPerCoach && passCount > remainingInCoach) {
                        if (currentPos + remainingInCoach < cnfCap) {
                            if (wl.equals("GNWL")) gnP += remainingInCoach;
                            else if (wl.equals("PQWL")) pqP += remainingInCoach;
                            else tqP += remainingInCoach;
                        }
                    }
                }

                for (BookingRequest.PassengerDetail pd : r.getPassengers()) {
                    if (wl.equals("GNWL")) assignStatus(pd, gnP++, gnC, gnR, "GNWL", p, seatsPerCoach, 0, 0);
                    else if (wl.equals("PQWL")) assignStatus(pd, pqP++, pqC, 0, "PQWL", p, seatsPerCoach, gnC, 0);
                    else assignStatus(pd, tqP++, tqC, 0, "TQWL", p, seatsPerCoach, gnC + pqC, 0);
                }
            }
        }
    }

    private static void assignStatus(BookingRequest.PassengerDetail pd, int pos, int cnfCap, int racCap, String wlPrefix, String pPrefix, int perCoach, int cnfOffset, int racOffset) {
        if (pos < cnfCap) { 
            pd.currentStatus = "CNF"; 
            int physicalSeat = pos + cnfOffset;
            int coachNum = (physicalSeat / perCoach) + 1;
            int seatInCoach = (physicalSeat % perCoach) + 1;
            pd.currentCoachSeat = pPrefix + coachNum + "/" + seatInCoach; 
        } 
        else if (pos < cnfCap + racCap) { 
            int globalRac = (pos - cnfCap) + racOffset + 1;
            pd.currentStatus = "RAC " + globalRac; 
            pd.currentCoachSeat = "N/A"; 
        }
        else { 
            pd.currentStatus = wlPrefix + " " + (pos - cnfCap - racCap + 1); 
            pd.currentCoachSeat = "N/A"; 
        }
        
        if (pd.bookingStatus == null) { 
            pd.bookingStatus = pd.currentStatus; 
            pd.bookingCoachSeat = pd.currentCoachSeat; 
        }
    }
}