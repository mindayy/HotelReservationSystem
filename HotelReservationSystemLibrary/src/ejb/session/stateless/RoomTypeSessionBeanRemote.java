/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Remote
public interface RoomTypeSessionBeanRemote {
    
    public Long createNewRoomType(RoomType roomType);
    
    public void updateRoomType(RoomType roomType);
    
    public List<RoomType> viewAllRoomTypes();
    
    public RoomType retrieveRoomTypeById(Long roomTypeId) throws RoomTypeNotFoundException;
    
    public void deleteRoomType(RoomType roomTypeToDelete);
    
}
