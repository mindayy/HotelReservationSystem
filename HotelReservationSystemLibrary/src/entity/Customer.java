/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;


/**
 *
 * @author min
 */
@Entity
public class Customer extends Guest {

    @Column(nullable = false, length = 64, unique = true)
    private String email;
    @Column(nullable = false, length = 32, unique = true)
    private int phoneNum;
    @Column(nullable = false, length = 32, unique = true)
    private String passportNum;

    public Customer() {
    }

    public Customer(String email, int phoneNum, String passportNum) {
        this.email = email;
        this.phoneNum = phoneNum;
        this.passportNum = passportNum;
    }
    

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phoneNum
     */
    public int getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phoneNum the phoneNum to set
     */
    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * @return the passportNum
     */
    public String getPassportNum() {
        return passportNum;
    }

    /**
     * @param passportNum the passportNum to set
     */
    public void setPassportNum(String passportNum) {
        this.passportNum = passportNum;
    }



}
