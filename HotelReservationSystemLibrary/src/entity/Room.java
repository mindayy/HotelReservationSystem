/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.RoomStatusEnum;
import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author kaixin
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    @Column(nullable = false, length = 4, unique = true)
    private String roomNumber;
    
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatusEnum roomStatus;
    
    @Column(nullable = false)
    private boolean isDeleted = false;  // Soft delete indicator but still need to kiv 

    @OneToMany(mappedBy = "room", cascade = {}, fetch = FetchType.LAZY)
    private List<ReserveRoom> reserveRoom;
    
    
    public Room() {
    }

    public Room(String roomNumber, RoomType roomType, RoomStatusEnum roomStatus) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.isDeleted = false;  // When created, the room is active (not deleted)
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
   
    public String getRoomNumber() {
        return roomNumber;
    }

    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

   
    public RoomType getRoomType() {
        return roomType;
    }

    
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    
    public RoomStatusEnum getRoomStatus() {
        return roomStatus;
    }

    
    public void setRoomStatus(RoomStatusEnum roomStatus) {
        this.roomStatus = roomStatus;
    }

    
    public boolean isIsDeleted() {
        return isDeleted;
    }

    
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    
    /**
     * @return the reserveRoom
     */
    public List<ReserveRoom> getReserveRoom() {
        return reserveRoom;
    }

    /**
     * @param reserveRoom the reserveRoom to set
     */
    public void setReserveRoom(List<ReserveRoom> reserveRoom) {
        this.reserveRoom = reserveRoom;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.room[ id=" + roomId + " ]";
    }
    
}
