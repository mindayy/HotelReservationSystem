/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
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
    public Long createNewRoomType(RoomRate roomRate) {
	em.persist(roomRate);
        em.flush();
 
        return roomRate.getRoomRateId();
    }
    
    @Override
    public RoomRate getRoomRateDetails(Long roomRateId) {
        return em.find(RoomRate.class, roomRateId);
    }
    
    @Override
    public List<RoomRate> viewAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        return query.getResultList();
    } 
    
    @Override
    public void updateRoomRate(Long roomRateId, String name, Long roomTypeId, Enum rateType, 
                                    BigDecimal ratePerNight, Date validFrom, Date validTo, 
                                    Boolean isDisabled) throws RoomTypeNotFoundException, RoomRateNotFoundException 
    {

        RoomRate roomRateToUpdate = em.find(RoomRate.class, roomRateId);
        if (roomRateToUpdate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + roomRateId + " not found."); 
        }

        RoomType roomType = em.find(RoomType.class, roomTypeId); 
        if (roomType == null) {
            throw new RoomTypeNotFoundException("Room Type with ID " + roomTypeId + " not found."); 
        }

        roomRateToUpdate.setRoomRateName(name);
        roomRateToUpdate.setRoomType(roomType);
        roomRateToUpdate.setRateType(rateType);
        roomRateToUpdate.setRatePerNight(ratePerNight); 
        roomRateToUpdate.setValidFrom(validFrom);
        roomRateToUpdate.setValidTo(validTo);
    }
    
    @Override
    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException {
        RoomType roomRateToDelete = em.find(RoomType.class, roomRateId);
        if (roomRateToDelete != null) {
            if (isRoomRateInUse(roomRateId)) {
                roomRateToDelete.setIsDisabled(true);
                em.merge(roomRateToDelete);
            } else {
                em.remove(roomRateToDelete);
            }
        } else {
            throw new RoomRateNotFoundException("Room Rate with ID " + roomRateId + " not found.");
        }
    }

    private boolean isRoomRateInUse(Long roomRateId) {
        //logic to be inserted
        return false;

    }

    
}
