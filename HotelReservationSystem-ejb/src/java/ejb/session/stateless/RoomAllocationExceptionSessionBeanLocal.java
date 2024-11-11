/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Local;
import entity.ReserveRoom;


/**
 *
 * @author kaixin
 */
@Local
public interface RoomAllocationExceptionSessionBeanLocal {
    
    public void createRoomAllocationException(ReserveRoom reserveRoom, String exceptionType, String message);
    
    public List<Exception> retrieveRoomAllocationExceptions();
    
}
