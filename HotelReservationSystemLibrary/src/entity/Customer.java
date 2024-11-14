/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 *
 * @author min
 */
@Entity

public class Customer extends Guest {

    @Column(nullable = false, length = 64)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    @Column(nullable = false)
    private boolean isLoggedIn;
    


    public Customer() {
    }

    public Customer(String email,String username, String password, boolean isLoggedIn) {
        super(email);
        this.username = username;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }


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
    

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    @Override
    public String toString() {
        return "entity.Customer[ id=" + getGuestId() + ", username=" + username + " ]";
    }

}
