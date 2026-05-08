package com.sathish.thodar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class ParseHelper {
    
    // Validate Email format
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    // Validate Mobile Number (Must be exactly 10 digits)
    public static boolean isValidMobile(String mobileNo) {
        return mobileNo != null && mobileNo.matches("\\d{10}");
    }

    // Convert Date String (dd-MM-yyyy) to Epoch Milliseconds
    public static Long dateToEpoch(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false); // Strict date validation (e.g., prevents 32-13-2026)
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            return null; // Returns null if format is wrong
        }
    }

    // Convert Epoch Milliseconds to Date String (dd-MM-yyyy)
    public static String epochToDateString(Long epochTime) {
        if (epochTime == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date(epochTime));
    }

    // Convert Epoch Milliseconds to Date & Time String (dd-MM-yyyy HH:mm)
    public static String epochToDateTimeString(Long epochTime) {
        if (epochTime == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return sdf.format(new Date(epochTime));
    }
}