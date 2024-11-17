/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Local;
import entity.ReservationRoom;
import entity.ExceptionReport;


/**
 *
 * @author kaixin
 */
@Local
public interface ExceptionReportSessionBeanLocal {
    
    public void logException(Long reservationRoomId, String errorMessage);
 
    public List<String> generateRoomAllocationExceptionReport();
    
}
