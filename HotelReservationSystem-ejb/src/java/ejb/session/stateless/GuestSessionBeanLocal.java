/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author min
 */
@Local
public interface GuestSessionBeanLocal {

    public void registerGuest(String email, String username, String password);
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;
    
    public Customer guestLogin(String username, String password) throws InvalidLoginCredentialException;
    
    public Reservation viewReservationDetails(Long reservationId) throws ReservationNotFoundException;
    
    public List<Reservation> viewAllMyReservations(Long guestId);
    
    
}
