/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author min
 */
@Remote
public interface ReservationSessionBeanRemote {
    
    public void checkInGuest(Long reservationId) throws ReservationNotFoundException;
    
    public void checkOutGuest(Long reservationId) throws ReservationNotFoundException;
    
}
