/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class ReservationRoom implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationRoomId;
    
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Room room;
    
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Reservation reservation;
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reservedFrom;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reservedTo;
    
    @Column(nullable = false)
    private boolean roomAllocated = false;  // To track whether the room has been allocated

    @OneToMany(mappedBy = "reservationRoom", cascade = {}, fetch = FetchType.LAZY)
    private List<ExceptionReport> exceptions;  // List of exceptions for this reservation

    public ReservationRoom() {
    }

    public ReservationRoom(Room room, Reservation reservation, Date reservedFrom, Date reservedTo) {
        this.room = room;
        this.reservation = reservation;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.roomAllocated = false;
    }
    

    public Long getReservationRoomId() {
        return reservationRoomId;
    }

    public void setReservationRoomId(Long reservationRoomId) {
        this.reservationRoomId = reservationRoomId;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Reservation getReservation() {
        return reservation;
    }
    
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    public Date getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(Date reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public Date getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(Date reservedTo) {
        this.reservedTo = reservedTo;
    }
    

    public boolean isRoomAllocated() {
        return roomAllocated;
    }


    public void setRoomAllocated(boolean roomAllocated) {
        this.roomAllocated = roomAllocated;
    }

    
    public List<ExceptionReport> getExceptions() {
        return exceptions;
    }
    

    public void setExceptions(List<ExceptionReport> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationRoomId != null ? reservationRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationRoomId fields are not set
        if (!(object instanceof ReservationRoom)) {
            return false;
        }
        ReservationRoom other = (ReservationRoom) object;
        if ((this.reservationRoomId == null && other.reservationRoomId != null) || (this.reservationRoomId != null && !this.reservationRoomId.equals(other.reservationRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.reserveRoom[ id=" + reservationRoomId + " ]";
    }
    
}
