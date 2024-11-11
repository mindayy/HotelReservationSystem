/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Remote;
import entity.ReserveRoom;
import entity.RoomAllocationException;

/**
 *
 * @author kaixin
 */
@Remote
public interface RoomAllocationExceptionSessionBeanRemote {
    
    public void createRoomAllocationException(ReserveRoom reserveRoom, String exceptionType, String message);
    
    public List<RoomAllocationException> retrieveRoomAllocationExceptions();
    
    
}
