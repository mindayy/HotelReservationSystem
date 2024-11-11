/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
import javax.persistence.ManyToOne;

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

    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReserveRoom reserveRoom;  // Relationship with ReserveRoom entity

    @Column(nullable = false)
    private String exceptionType;  // Type of exception (upgrade/no room)

    public RoomAllocationException() {
    }

    public RoomAllocationException(String message, ReserveRoom reserveRoom, String exceptionType) {
        this.message = message;
        this.reserveRoom = reserveRoom;
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


    public ReserveRoom getReserveRoom() {
        return reserveRoom;
    }


    public void setReserveRoom(ReserveRoom reserveRoom) {
        this.reserveRoom = reserveRoom;
    }


    public String getExceptionType() {
        return exceptionType;
    }


    public void setExceptionType(String exceptionType) {
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