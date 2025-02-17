package com.uk.uk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
@Entity
@Getter
@Setter
public class AuthEntity {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String userEmail;
    private String password;
    private String tempOtp;

    private String userPrivilege;

    public String getUserPrivilege() {
        return userPrivilege;
    }

    public void setUserPrivilege(String userPrivilege) {
        this.userPrivilege = userPrivilege;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTempOtp() {
        return tempOtp;
    }

    public void setTempOtp(String tempOtp) {
        this.tempOtp = tempOtp;
    }
}

