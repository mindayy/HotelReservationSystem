/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.RoomAllocationExceptionTypeEnum;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author kaixin
 */
@Entity
public class RoomAllocationException implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionId;
    
    @Column(nullable = false)
    private String message;  // RoomAllocationException message

    @OneToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReservationRoom reservationRoom;  // Relationship with ReserveRoom entity

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomAllocationExceptionTypeEnum exceptionType;

    public RoomAllocationException() {
    }

    public RoomAllocationException(String message, ReservationRoom reservationRoom, RoomAllocationExceptionTypeEnum exceptionType) {
        this.message = message;
        this.reservationRoom = reservationRoom;
        this.exceptionType = exceptionType;
    }
  
    public Long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Long exceptionId) {
        this.exceptionId = exceptionId;
    }
    

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public ReservationRoom getReservationRoom() {
        return reservationRoom;
    }


    public void setReservationRoom(ReservationRoom reservationRoom) {
        this.reservationRoom = reservationRoom;
    }


    public RoomAllocationExceptionTypeEnum getExceptionType() {
        return exceptionType;
    }


    public void setExceptionType(RoomAllocationExceptionTypeEnum exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionId != null ? exceptionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionId fields are not set
        if (!(object instanceof RoomAllocationException)) {
            return false;
        }
        RoomAllocationException other = (RoomAllocationException) object;
        if ((this.exceptionId == null && other.exceptionId != null) || (this.exceptionId != null && !this.exceptionId.equals(other.exceptionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.exception[ id=" + exceptionId + " ]";
    }
    
}