/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
            // Handle case where room type is not found
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }
    }
    
    
        
}
