/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author min
 */
@Remote
public interface ReservationSessionBeanRemote {
    
    public void checkInGuest(Long reservationId) throws ReservationNotFoundException;
    
    public void checkOutGuest(Long reservationId) throws ReservationNotFoundException;
    
    public Reservation reserveRoom(Long guestId, List<Long> roomIds, Date checkInDate, Date checkOutDate) 
            throws GuestNotFoundException, ReservationNotFoundException, RoomNotAvailableException;

    public BigDecimal reservationAmt(Long roomTypeId, Date checkInDate, Date checkOutDate);
    
    public List<Reservation> getAllReservationsForCustomer(Long customerId);
    
    public Reservation getReservationDetails(Long reservationId) throws ReservationNotFoundException;
}
