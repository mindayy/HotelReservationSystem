/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author min
 */
@Local
public interface ReservationSessionBeanLocal {

    public void checkInGuest(Long reservationId) throws ReservationNotFoundException;

    public void checkOutGuest(Long reservationId) throws ReservationNotFoundException;
    
    public Reservation reserveRoom(Long guestId, List<Long> roomIds, Date checkInDate, Date checkOutDate) 
            throws GuestNotFoundException, ReservationNotFoundException, RoomNotAvailableException;
    
    
    
}
