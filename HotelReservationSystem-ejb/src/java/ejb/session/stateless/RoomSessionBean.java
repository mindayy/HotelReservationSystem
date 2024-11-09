/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import enums.RoomStatusEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomNotFoundException;

/**
 *
 * @author kaixin
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanRemote, RoomSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void createRoom(String roomNumber, RoomType roomType, RoomStatusEnum roomStatus) {
        Room room = new Room(roomNumber, roomType, roomStatus);
        em.persist(room);  // Persist the new room
    }

    @Override
    public void updateRoom(Long roomId, RoomType roomType, RoomStatusEnum roomStatus) throws RoomNotFoundException {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            room.setRoomTypeEntity(roomType);
            room.setRoomStatus(roomStatus);
            em.merge(room);  // Merge the changes
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " not found.");
        }
    }

    @Override
    public void deleteRoom(Long roomId) throws RoomNotFoundException {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            // Assume the room is not currently used
            room.setIsDeleted(true);  // Mark the room as deleted
            em.merge(room);  // Update the record
        } else {
            throw new RoomNotFoundException("Room ID " + roomId + " not found.");
        }
    }

    @Override
    public List<Room> retrieveAllRooms() {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.isDeleted = false");
        return query.getResultList();
    }
}
