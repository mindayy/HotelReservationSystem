/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.ExceptionReport;
import entity.RoomRate;
import entity.RoomType;
import enums.ReservationStatus;
import static enums.ReservationStatus.CHECKEDIN;
import static enums.ReservationStatus.CHECKEDOUT;
import enums.RoomStatusEnum;
import static java.lang.Boolean.FALSE;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import util.exception.GuestNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;
import util.exception.RoomAllocationException;

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
    
    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBean;
    
    @Override
    public void checkInGuest(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Room Reservation with ID " + reservationId + " not found.");
        }

        // Check for room allocation exceptions 
        /*if (... conditions for exception ...) {
            throw new ExceptionReport();
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
    public BigDecimal reservationAmt(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);

        while (calendar.getTime().before(checkOutDate)) {
            Date currentDate = new Date(calendar.getTime().getTime()); 
            BigDecimal roomRatePerNight = getApplicableRoomRate(roomTypeId, currentDate, currentDate);

            totalAmount = totalAmount.add(roomRatePerNight);

            calendar.add(Calendar.DATE, 1);
        }
        return totalAmount;

    }
    
    @Override
    public List<Reservation> getAllReservationsForCustomer(Long customerId) {
        TypedQuery<Reservation> query = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.guest.guestId = :customerId", Reservation.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    @Override
    public Reservation getReservationDetails(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.");
        }
        return reservation;
    }
    
    @Override
    public Reservation reserveRoom(Long guestId, Long roomId, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, RoomNotAvailableException, ReservationNotFoundException {
        Customer guest = em.find(Customer.class, guestId);
        Room room = em.find(Room.class, roomId);
        if (guest == null) {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist.");
        }
        if (room.getRoomStatus() != RoomStatusEnum.AVAILABLE) {
            throw new RoomNotAvailableException("Room ID " + roomId + " is not available.");
        }
        
        Reservation newReservation = new Reservation();
        newReservation.setGuest(guest);
        newReservation.setCheckInDate(checkInDate);
        newReservation.setCheckOutDate(checkOutDate);
        newReservation.setRoomType(room.getRoomType());
        newReservation.setReservationStatus(ReservationStatus.RESERVED);

        // Create and set ReservationRoom
        ReservationRoom reservationRoom = new ReservationRoom();
        reservationRoom.setRoom(room);
        reservationRoom.setReservedFrom(checkInDate);
        reservationRoom.setReservedTo(checkOutDate);
        reservationRoom.setRoomAllocated(FALSE);
        reservationRoom.setReservation(newReservation);
        
        newReservation.getReservationRooms().add(reservationRoom);
        


        BigDecimal totalAmount = reservationAmt(room.getRoomType().getRoomTypeId(), checkInDate, checkOutDate); // Assuming this method exists
        newReservation.setReservationAmt(totalAmount);

        Calendar cal = Calendar.getInstance();
        if (checkInDate.equals(new Date()) && cal.get(Calendar.HOUR_OF_DAY) >= 2) {
            newReservation.setReservationStatus(ReservationStatus.CHECKEDIN); // Immediate check-in after 2 AM
        } else {
            newReservation.setReservationStatus(ReservationStatus.RESERVED);
        }

        em.persist(newReservation); // Persist the reservation
        return newReservation;
    }

    // Helper method to get the applicable room rate based on the date
    private BigDecimal getApplicableRoomRate(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        List<RoomRate> roomRates = roomRateSessionBean.getRoomRatesForRoomType(roomTypeId, checkInDate, checkOutDate);

        RoomRate promotionRate = null;
        RoomRate peakRate = null;
        RoomRate normalRate = null;

        for (RoomRate rate : roomRates) {
            switch (rate.getRateType()) {
                case PROMOTION:
                    promotionRate = rate;
                    break;
                case PEAK:
                    peakRate = rate;
                    break;
                case NORMAL:
                    normalRate = rate;
                    break;
            }
        }

        // Determine the applicable rate:
        if (promotionRate != null) {
            return promotionRate.getRatePerNight(); // Promotion rate takes precedence
        } else if (peakRate != null) {
            return peakRate.getRatePerNight(); // Peak rate takes precedence over normal
        } else if (normalRate != null) {
            return normalRate.getRatePerNight(); // Fallback to normal rate
        } else {
            throw new IllegalStateException("No rate defined for room type " + roomTypeId + " on the selected dates.");
        }
    }

    

    @Override
    public Long createReservation(Reservation reservation, List<ReservationRoom> reservationRooms) {
        em.persist(reservation);

        for (ReservationRoom reservationRoom : reservationRooms) {
            reservationRoom.setReservation(reservation);
            em.persist(reservationRoom);
        }

        return reservation.getReservationId();
    }

    

}
