/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import javax.ejb.Local;
import util.exception.ReservationNotFoundException;
import util.exception.RoomAllocationException;

/**
 *
 * @author min
 */
@Local
public interface ReservationSessionBeanLocal {

    public void checkInGuest(Long reservationId) throws RoomAllocationException, ReservationNotFoundException;

    public void checkOutGuest(Long reservationId) throws ReservationNotFoundException;
    
}
