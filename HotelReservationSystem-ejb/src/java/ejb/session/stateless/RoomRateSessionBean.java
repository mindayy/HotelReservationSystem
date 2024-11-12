/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import enums.RateTypeEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewRoomRate(RoomRate roomRate, Long roomTypeId) {
        
        // do association
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType == null) {
            throw new IllegalArgumentException("RoomType with ID " + roomTypeId + " does not exist.");
        }
        roomRate.setRoomType(roomType);
        roomType.getRoomRates().add(roomRate);
        
        // persist room rate
	em.persist(roomRate);
        em.flush();

        return roomRate.getRoomRateId();
    }
    
    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        if (roomRate != null) {
            return roomRate;
        }
        else
        {
            throw new RoomRateNotFoundException("Room Rate with ID " + roomRateId + " not found.");
        }
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        return query.getResultList();
    } 
    
    @Override
    public void updateRoomRate(RoomRate roomRate)  {
        em.merge(roomRate); 

    }
    
    @Override
    public void deleteRoomRate(RoomRate roomRateToDelete) {
        if (roomRateToDelete != null) {
            if (isRoomRateInUse(roomRateToDelete)) {
                roomRateToDelete.setIsDisabled(true);
                em.merge(roomRateToDelete);
            } else {
                em.remove(roomRateToDelete);
            }
        }
    }

    private boolean isRoomRateInUse(RoomRate roomRateToDelete) {
        //logic to be inserted
        return false;

    }
    
    @Override
    public Long getRoomTypeIdForRoomRate(Long roomRateId) throws RoomRateNotFoundException {
        try {
            RoomRate roomRate = em.find(RoomRate.class, roomRateId);
            if (roomRate != null && roomRate.getRoomType() != null) {
                return roomRate.getRoomType().getRoomTypeId();
            } else {
                throw new RoomRateNotFoundException("Room rate or associated room type not found!");
            }
        } catch (Exception e) {
            throw new RoomRateNotFoundException("An error occurred while retrieving room type: " + e.getMessage());
        }
    }

    
}
