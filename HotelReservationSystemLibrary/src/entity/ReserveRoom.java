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

/**
 *
 * @author kaixin
 */
@Entity
public class ReserveRoom implements Serializable {

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservedRoomId;
    
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

    @OneToMany(mappedBy = "reserveRoom", cascade = {}, fetch = FetchType.LAZY)
    private List<Exception> exceptions;  // List of exceptions for this reservation

    public ReserveRoom() {
    }

    public ReserveRoom(Room room, Reservation reservation, Date reservedFrom, Date reservedTo) {
        this.room = room;
        this.reservation = reservation;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
        this.roomAllocated = false;
    }
    

    public Long getReservedRoomId() {
        return reservedRoomId;
    }

    public void setReservedRoomId(Long reservedRoomId) {
        this.reservedRoomId = reservedRoomId;
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

    
    public List<Exception> getExceptions() {
        return exceptions;
    }
    

    public void setExceptions(List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservedRoomId != null ? reservedRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservedRoomId fields are not set
        if (!(object instanceof ReserveRoom)) {
            return false;
        }
        ReserveRoom other = (ReserveRoom) object;
        if ((this.reservedRoomId == null && other.reservedRoomId != null) || (this.reservedRoomId != null && !this.reservedRoomId.equals(other.reservedRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.reserveRoom[ id=" + reservedRoomId + " ]";
    }
    
}
