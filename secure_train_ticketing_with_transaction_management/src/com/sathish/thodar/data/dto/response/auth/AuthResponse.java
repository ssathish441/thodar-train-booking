package com.sathish.thodar.data.dto.response.auth;

import com.sathish.thodar.data.dto.enums.Role;

public class AuthResponse {
    private Long id;
    private String name;
    private Role role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}