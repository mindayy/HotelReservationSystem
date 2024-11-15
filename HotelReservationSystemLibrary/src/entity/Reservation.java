/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.ReservationStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author min
 */
@Entity
public class Reservation implements Serializable {

    /**
     * @return the reservationRooms
     */
    public List<ReservationRoom> getReservationRooms() {
        return reservationRooms;
    }

    /**
     * @param reservationRooms the reservationRooms to set
     */
    public void setReservationRooms(List<ReservationRoom> reservationRooms) {
        this.reservationRooms = reservationRooms;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date checkInDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date checkOutDate;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal reservationAmt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus reservationStatus;
    
    // relationships
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Guest guest;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Partner partner;
    
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ReservationRoom> reservationRooms;
    
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @ManyToMany
    @JoinColumn(nullable = false)
    private List<RoomRate> roomRates;

    public Reservation() {
        reservationRooms = new ArrayList<>();
        roomRates = new ArrayList<>();
    }
    
    // overloaded constructor
    public Reservation(Date checkInDate, Date checkOutDate, BigDecimal reservationAmt, ReservationStatus reservationStatus) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationAmt = reservationAmt;
        this.reservationStatus = reservationStatus;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    
    public Date getCheckInDate() {
        return checkInDate;
    }


    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }


    public Date getCheckOutDate() {
        return checkOutDate;
    }


    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

  
    public BigDecimal getReservationAmt() {
        return reservationAmt;
    }

 
    public void setReservationAmt(BigDecimal reservationAmt) {
        this.reservationAmt = reservationAmt;
    }
    
  
    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

  
    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }
 
    
    public Enum getReservationStatus() {
        return reservationStatus;
    }


    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }


    public Guest getGuest() {
        return guest;
    }


    public void setGuest(Customer guest) {
        this.guest = guest;
    }


    public Partner getPartner() {
        return partner;
    }

    
    public void setPartner(Partner partner) {
        this.partner = partner;
    }


    public List<ReservationRoom> getReserveRooms() {
        return getReservationRooms();
    }


    public void setReserveRooms(List<ReservationRoom> reservationRooms) {
        this.setReservationRooms(reservationRooms);
    }


    public RoomType getRoomType() {
        return roomType;
    }


    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
}
