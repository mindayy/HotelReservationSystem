/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Local;
import entity.ReservationRoom;
import entity.RoomAllocationException;
import enums.RoomAllocationExceptionTypeEnum;


/**
 *
 * @author kaixin
 */
@Local
public interface RoomAllocationExceptionSessionBeanLocal {
    
    public void createRoomAllocationException(ReservationRoom reservationRoom, RoomAllocationExceptionTypeEnum exceptionType, String message);
    
    public RoomAllocationException retrieveRoomAllocationExceptionById(Long exceptionId);
    
    public List<RoomAllocationException> retrieveAllRoomAllocationExceptions();
    
    public List<String> generateRoomAllocationExceptionReport();
    
}
