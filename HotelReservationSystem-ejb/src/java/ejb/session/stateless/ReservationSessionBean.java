/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import enums.ReservationStatus;
import static enums.ReservationStatus.CHECKEDIN;
import static enums.ReservationStatus.CHECKEDOUT;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;
import util.exception.RoomNotFoundException;

/**
 *
 * @author min
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private ReservationRoomSessionBeanLocal reservationRoomSessionBean;
    
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
    
    @Override

    public Reservation reserveRoom(Long guestId, List<Long> roomIds, Date checkInDate, Date checkOutDate) 
            throws GuestNotFoundException, ReservationNotFoundException, RoomNotAvailableException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest == null) {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist.");
        }

        Reservation newReservation = new Reservation();
        newReservation.setGuest(guest);
        newReservation.setCheckInDate(checkInDate);
        newReservation.setCheckOutDate(checkOutDate);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // For each room, check availability and create reservation room entry
        for (Long roomId : roomIds) {
            // Declare that the method can throw RoomNotAvailableException
            ReservationRoom reservationRoom = reservationRoomSessionBean.reserveRoom(roomId, newReservation, checkInDate, checkOutDate);

            // Calculate the total amount based on the room rate and stay duration
            Room room = reservationRoom.getRoom();
            BigDecimal roomRate = room.getRoomType().getCurrentRoomRate();
            long numNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
            totalAmount = totalAmount.add(roomRate.multiply(new BigDecimal(numNights)));
        }

        newReservation.setTotalAmount(totalAmount);

        Calendar cal = Calendar.getInstance();
        if (checkInDate.equals(new Date()) && cal.get(Calendar.HOUR_OF_DAY) >= 2) {
            newReservation.setReservationStatus(ReservationStatus.CHECKEDIN); // Immediate check-in after 2 AM
        } else {
            newReservation.setReservationStatus(ReservationStatus.RESERVED);
        }

        em.persist(newReservation); // Persist the reservation
        return newReservation;
    }

}
