/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import enums.RateTypeEnum;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Remote
public interface RoomRateSessionBeanRemote {
    
    public Long createNewRoomRate(RoomRate roomRate);
        
    public List<RoomRate> viewAllRoomRates();
    
    public RoomRate retrieveRoomRateById(Long roomRateId) throws RoomRateNotFoundException;
    
    public void updateRoomRate(RoomRate roomRate);
    
    public void deleteRoomRate(RoomRate roomRateToDelete);
}
