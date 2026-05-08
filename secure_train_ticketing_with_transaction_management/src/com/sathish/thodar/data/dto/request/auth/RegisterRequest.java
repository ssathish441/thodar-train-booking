package com.sathish.thodar.data.dto.request.auth;

import com.sathish.thodar.data.dto.enums.Role;

public class RegisterRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String mobileNo;
    private Role role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    private double walletBalance = 10000.0;

    
    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
}