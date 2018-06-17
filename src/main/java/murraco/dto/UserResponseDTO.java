package murraco.dto;

import murraco.model.Role;

import java.util.List;

public class UserResponseDTO {

    private Integer id;

    private String username;

    private String email;

    private String premiumstatus;

    private List<Role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPremiumstatus() {
        return premiumstatus;
    }

    public void setPremiumstatus(String premiumstatus) {
        this.premiumstatus = premiumstatus;
    }
}
