/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
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
public class ExceptionReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionId;
    
    @Column(nullable = false)
    private String errorMessage;  // ExceptionReport message

    @OneToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ReservationRoom reservationRoom;  // Relationship with ReserveRoom entity


    public ExceptionReport() {
    }

    public ExceptionReport(String errorMessage, ReservationRoom reservationRoom) {
        this.errorMessage = errorMessage;
        this.reservationRoom = reservationRoom;
    }

  
    public Long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Long exceptionId) {
        this.exceptionId = exceptionId;
    }
    

    public String getErrorMessage() {
        return errorMessage;
    }


    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }


    public ReservationRoom getReservationRoom() {
        return reservationRoom;
    }


    public void setReservationRoom(ReservationRoom reservationRoom) {
        this.reservationRoom = reservationRoom;
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
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
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