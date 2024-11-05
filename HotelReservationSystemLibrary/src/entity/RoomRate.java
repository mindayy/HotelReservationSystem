/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author min
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomRateId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Enum rateType;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal ratePerNight;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date validFrom;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date validTo;
    
    // relationships
    @ManyToOne(optional = false, cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @ManyToMany(mappedBy = "roomRates")
    private List<Reservation> reservations;
    
    
    public RoomRate() {
    }

    public RoomRate(Enum rateType, BigDecimal ratePerNight, Date validFrom, Date validTo, RoomType roomType, List<Reservation> reservations) {
        this.rateType = rateType;
        this.ratePerNight = ratePerNight;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.roomType = roomType;
        this.reservations = reservations;
    }
    
    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }
    
    /**
     * @return the rateType
     */
    public Enum getRateType() {
        return rateType;
    }

    /**
     * @param rateType the rateType to set
     */
    public void setRateType(Enum rateType) {
        this.rateType = rateType;
    }

    /**
     * @return the ratePerNight
     */
    public BigDecimal getRatePerNight() {
        return ratePerNight;
    }

    /**
     * @param ratePerNight the ratePerNight to set
     */
    public void setRatePerNight(BigDecimal ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    /**
     * @return the validFrom
     */
    public Date getValidFrom() {
        return validFrom;
    }

    /**
     * @param validFrom the validFrom to set
     */
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    /**
     * @return the validTo
     */
    public Date getValidTo() {
        return validTo;
    }

    /**
     * @param validTo the validTo to set
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }

    /**
     * @return the roomType
     */
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @param roomType the roomType to set
     */
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    /**
     * @return the reservation
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
    
}
