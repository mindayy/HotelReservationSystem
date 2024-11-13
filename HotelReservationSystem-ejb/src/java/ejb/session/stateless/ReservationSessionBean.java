/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import static enums.ReservationStatus.CHECKEDIN;
import static enums.ReservationStatus.CHECKEDOUT;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author min
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void checkInGuest(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Room Reservation with ID " + reservationId + " not found.");
        }

        // Check for room allocation exceptions 
        /*if (... conditions for exception ...) {
            throw new RoomAllocationException();
        }*/

        // If no exception, update reservation status and potentially other details:
        reservation.setReservationStatus(CHECKEDIN);
        // ... (Update check-in time, etc.)

        em.merge(reservation); // Persist the changes
    }
    
    @Override
    public void checkOutGuest(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Room Reservation with ID " + reservationId + " not found.");
        }
        reservation.setReservationStatus(CHECKEDOUT);
        em.merge(reservation);
    }
    
}
