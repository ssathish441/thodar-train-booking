package com.sathish.thodar.features.auth;

import com.sathish.thodar.util.ConsoleInput;
import com.sathish.thodar.util.ParseHelper;
import com.sathish.thodar.data.repository.ThodarDB;
import com.sathish.thodar.data.dto.enums.Role;
import com.sathish.thodar.data.dto.request.auth.LoginRequest;
import com.sathish.thodar.data.dto.request.auth.RegisterRequest;
import com.sathish.thodar.data.dto.response.auth.AuthResponse;
import com.sathish.thodar.features.admin.AdminView;
import com.sathish.thodar.features.passenger.PassengerView;

public class AuthView {

    private final ThodarDB db = ThodarDB.getInstance();

    public AuthView() {
        if (db.getUserByEmail("admin@thodar.com") == null) {
            RegisterRequest admin = new RegisterRequest();
            admin.setName("Super Admin"); admin.setEmail("admin@thodar.com"); admin.setPassword("admin123");
            admin.setMobileNo("9999999999"); admin.setRole(Role.ADMIN); db.addUser(admin);
        }
    }

    public void showLandingMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Passenger Login");
            System.out.println("2. Passenger Register");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            String choice = ConsoleInput.getString("Choice: ").trim();

            switch (choice) {
                case "1": handleLogin(Role.CUSTOMER); break;
                case "2": handleRegister(); break;
                case "3": handleLogin(Role.ADMIN); break;
                case "4": System.out.println("Thank you for using Thodar Railways!"); System.exit(0);
                default: System.out.println("[ERROR] Invalid option.");
            }
        }
    }

    private void handleRegister() {
        System.out.println("\n--- REGISTRATION ---");
        RegisterRequest newUser = new RegisterRequest();
        newUser.setName(ConsoleInput.getString("Name: "));
        String email;
        while(true) { email = ConsoleInput.getString("Email: "); if(ParseHelper.isValidEmail(email)) break; else System.out.println("[ERROR] Invalid Email!"); }
        newUser.setEmail(email); newUser.setPassword(ConsoleInput.getString("Password: "));
        String mobile;
        while(true) { mobile = ConsoleInput.getString("Mobile No (10 digits): "); if(ParseHelper.isValidMobile(mobile)) break; else System.out.println("[ERROR] Invalid Mobile!"); }
        newUser.setMobileNo(mobile); newUser.setRole(Role.CUSTOMER); 
        newUser.setWalletBalance(0.0); 
        db.addUser(newUser);
        System.out.println("[SUCCESS] Registered Successfully! Please login and recharge your wallet to book tickets.");
    }

    private void handleLogin(Role requiredRole) {
        System.out.println(requiredRole == Role.ADMIN ? "\n--- SECURE ADMIN PORTAL ---" : "\n--- PASSENGER LOGIN ---");
        LoginRequest req = new LoginRequest();
        req.setEmail(ConsoleInput.getString("Email: ")); req.setPassword(ConsoleInput.getString("Password: "));
        RegisterRequest userEntity = db.authenticateUser(req.getEmail(), req.getPassword());
        
        if (userEntity != null && userEntity.getRole() == requiredRole) {
            AuthResponse resp = new AuthResponse(); 
            resp.setId(userEntity.getId()); resp.setName(userEntity.getName()); resp.setRole(userEntity.getRole());
            System.out.println("\n[SUCCESS] Welcome, " + resp.getName() + "!");
            
            if (requiredRole == Role.ADMIN) {
                new AdminView().showAdminMenu();
            } else {
                new PassengerView(resp, userEntity).showPassengerMenu();
            }
        } else {
            System.out.println("[ERROR] Invalid Credentials!");
        }
    }
}