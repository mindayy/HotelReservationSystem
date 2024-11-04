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
import javax.persistence.OneToMany;

/**
 *
 * @author min
 */
@Entity
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomTypeId;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String description;
    @Column(nullable = false, length = 64)
    private int size;
    @Column(nullable = false, length = 64)
    private String bed;
    @Column(nullable = false, length = 64)
    private int capacity;
    @Column(nullable = false, length = 64)
    private String amenities;
    @Column(nullable = false, length = 64)
    private Boolean isDisabled;
    @Column(nullable = false, length = 64)
    private Boolean roomTypeStatus;
    
    // relationships
    @OneToMany(mappedBy = "roomType", cascade = {}, fetch = FetchType.LAZY)
    private List<Room> rooms;
    
    @OneToMany(mappedBy = "roomType", cascade = {}, fetch = FetchType.LAZY)
    private List<RoomRate> roomRates;
    
    @OneToMany(mappedBy = "roomType", cascade = {}, fetch = FetchType.LAZY)
    private List<Reservation> reservations;
    
    // empty constructor
    public RoomType() {
    }
    
    // overloaded constructor
    public RoomType(String name, String description, int size, String bed, int capacity, String amenities, Boolean isDisabled, Boolean roomTypeStatus, List<Room> rooms, List<RoomRate> roomRates) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.bed = bed;
        this.capacity = capacity;
        this.amenities = amenities;
        this.isDisabled = isDisabled;
        this.roomTypeStatus = roomTypeStatus;
        this.rooms = rooms;
        this.roomRates = roomRates;
    }   
    
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the bed
     */
    public String getBed() {
        return bed;
    }

    /**
     * @param bed the bed to set
     */
    public void setBed(String bed) {
        this.bed = bed;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the amenities
     */
    public String getAmenities() {
        return amenities;
    }

    /**
     * @param amenities the amenities to set
     */
    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the roomTypeStatus
     */
    public Boolean getRoomTypeStatus() {
        return roomTypeStatus;
    }

    /**
     * @param roomTypeStatus the roomTypeStatus to set
     */
    public void setRoomTypeStatus(Boolean roomTypeStatus) {
        this.roomTypeStatus = roomTypeStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ id=" + roomTypeId + " ]";
    }
    
}
