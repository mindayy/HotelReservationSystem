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
    public void updateRoomRate(RoomRate roomRate, String name, Long newRoomTypeId, BigDecimal newRatePerNight, RateTypeEnum newRateType, Date startDate, Date endDate) {
        RoomRate managedRoomRate = em.find(RoomRate.class, roomRate.getRoomRateId());

        if (managedRoomRate != null) {
            if (name != null) {
                managedRoomRate.setRoomRateName(name);
            }

            if (newRatePerNight != null) {
                managedRoomRate.setRatePerNight(newRatePerNight);
            }

            if (newRateType != null) {
                managedRoomRate.setRateType(newRateType);
            }

            if (!managedRoomRate.getRoomType().getRoomTypeId().equals(newRoomTypeId)) {
                RoomType newRoomType = em.find(RoomType.class, newRoomTypeId);   

                RoomType currentRoomType = managedRoomRate.getRoomType();
                if (currentRoomType != null) {
                    currentRoomType.getRoomRates().remove(managedRoomRate);
                }

                managedRoomRate.setRoomType(newRoomType);
                newRoomType.getRoomRates().add(managedRoomRate);
                em.merge(newRoomType);
            }
            
            if (startDate != null) {
                managedRoomRate.setValidFrom(startDate);
            }
            
            if (endDate != null) {
                managedRoomRate.setValidTo(endDate);
            }

            em.merge(managedRoomRate);
        }
    }
    
    @Override
    public void deleteRoomRate(RoomRate roomRateToDelete) {
        if (roomRateToDelete != null) {
            if (isRoomRateInUse(roomRateToDelete)) {
                roomRateToDelete.setIsDisabled(true);
                em.merge(roomRateToDelete);
            } else {
                roomRateToDelete = em.merge(roomRateToDelete);
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
    
    @Override
    public List<RoomRate> getRoomRatesForRoomType(Long roomTypeId, Date checkInDate, Date checkOutDate) {
    // Query to retrieve the room rates based on room type and the provided date range, 
    // including rates where validFrom and validTo are null
    Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomType.roomTypeId = :roomTypeId "
            + "AND ((rr.validFrom IS NULL AND rr.validTo IS NULL) "
            + "OR (rr.validFrom <= :checkOutDate AND rr.validTo >= :checkInDate))");

    query.setParameter("roomTypeId", roomTypeId);
    query.setParameter("checkInDate", checkInDate);
    query.setParameter("checkOutDate", checkOutDate);

    // Return the list of room rates for the specified room type and date range
    return query.getResultList();
    }
    
}
