/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanRemote, RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRoomType(RoomType roomType) {
	em.persist(roomType);
        em.flush();
 
        return roomType.getRoomTypeId();
    }
    
    @Override
    public RoomType getRoomTypeDetails(Long roomTypeId) {
        return em.find(RoomType.class, roomTypeId);
    }
    
    @Override
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        return query.getResultList();
    } 
    
    @Override
    public void updateRoomType(Long roomTypeId, String newName, String newDescription, 
                             int newSize, String newBed, int newCapacity, 
                             String newAmenities, Boolean isDisabled, Boolean roomTypeStatus) throws RoomTypeNotFoundException {

        RoomType roomType = em.find(RoomType.class, roomTypeId);

        if (roomType != null) {
            roomType.setName(newName);
            roomType.setDescription(newDescription);
            roomType.setSize(newSize);
            roomType.setBed(newBed);
            roomType.setCapacity(newCapacity);
            roomType.setAmenities(newAmenities);
            roomType.setIsDisabled(isDisabled);
            roomType.setRoomTypeStatus(roomTypeStatus);

            em.merge(roomType); 
        } else {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }
    }
    
    @Override
    public void deleteRoomType(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomTypeToDelete = em.find(RoomType.class, roomTypeId);
        if (roomTypeToDelete != null) {
            if (isRoomTypeInUse(roomTypeId)) {
                roomTypeToDelete.setIsDisabled(true);
                em.merge(roomTypeToDelete);
            } else {
                em.remove(roomTypeToDelete);
            }
        } else {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }
    }
    
    private boolean isRoomTypeInUse(Long roomTypeId) {
        // logic to be added 
        return false; 
    }
        
}
