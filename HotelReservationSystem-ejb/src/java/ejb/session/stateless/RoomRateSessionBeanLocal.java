/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createNewRoomType(RoomRate roomRate);

    public RoomRate getRoomRateDetails(Long roomRateId);

    public void updateRoomRate(Long roomRateId, String name, Long roomTypeId, Enum rateType, 
            BigDecimal ratePerNight, Date validFrom, Date validTo, Boolean isDisabled) 
            throws RoomTypeNotFoundException, RoomRateNotFoundException;

    public List<RoomRate> viewAllRoomRates();

    public void deleteRoomRate(Long roomRateId) throws RoomRateNotFoundException;
    
}
