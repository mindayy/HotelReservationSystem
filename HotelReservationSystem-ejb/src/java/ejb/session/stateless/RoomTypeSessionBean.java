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
    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        if (roomType != null) {
            return roomType;
        }
        else
        {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found.");
        }
    }
    
    @Override
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        return query.getResultList();
    }
    
    @Override
    public void updateRoomType(RoomType roomType)  {
        em.merge(roomType); 

    }
    
    @Override
    public void deleteRoomType(RoomType roomTypeToDelete) {
        if (roomTypeToDelete != null) {
            if (isRoomTypeInUse(roomTypeToDelete)) {
                roomTypeToDelete.setIsDisabled(true);
                em.merge(roomTypeToDelete);
            } else {
                em.remove(roomTypeToDelete);
            }
        } 
    }
    
    private boolean isRoomTypeInUse(RoomType roomTypeToDelete) {
        // logic to be added 
        return false; 
    }
        
}
