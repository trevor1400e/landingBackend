package murraco.dto;

import murraco.model.Role;

import java.util.Arrays;
import java.util.List;

public class UserDataDTO {

    private String username;

    private String password;

    private String email;

    private List<Role> roles = Arrays.asList(Role.ROLE_CLIENT);

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

}
