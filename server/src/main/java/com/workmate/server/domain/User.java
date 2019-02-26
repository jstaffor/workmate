package com.workmate.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {

    @Id
    @Column(name="username")
    private String userName;
    @Column(name="password")
    private String password;
    @Column(name="role")
    private String role;
    @Column(name="full_name")
    private String fullName;
    @Column(name="country")
    private String country;
    @Column(name="enabled")
    private short enabled;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public short getEnabled()
    {
        return enabled;
    }

    public void setEnabled(short enabled)
    {
        this.enabled = enabled;
    }
}

