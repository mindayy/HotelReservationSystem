/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Remote;
import entity.ReservationRoom;
import entity.ExceptionReport;

/**
 *
 * @author kaixin
 */
@Remote
public interface ExceptionReportSessionBeanRemote {
    
    public void logException(Long reservationRoomId, String errorMessage);
     
    public List<String> generateRoomAllocationExceptionReport();
    
}
