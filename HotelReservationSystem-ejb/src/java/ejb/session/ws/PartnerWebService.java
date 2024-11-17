/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import entity.Customer;
import entity.Partner;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enums.ReservationStatus;
import enums.RoomStatusEnum;
import static java.lang.Boolean.FALSE;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author min
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @WebMethod
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            Partner partner = (Partner) query.getSingleResult();

            // Check if the partner is already logged in
            if (partner.isLoggedIn()) {
                System.out.println("Partner is already logged in.");
                return partner; 
            }

            // Check if the password matches
            if (partner.getPassword().equals(password)) {
                partner.setLoggedIn(true); 
                em.merge(partner);  
                return partner;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
    
    @WebMethod
    public Reservation viewReservationDetails(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " not found.");
        }
        return reservation;
    }
    
    @WebMethod
    public List<Reservation> viewAllMyReservations(Long partnerId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId");
        query.setParameter("partnerId", partnerId);
        return query.getResultList();
    }
    
    @WebMethod
    public List<RoomType> viewAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        List<RoomType> roomTypes = query.getResultList();
        for (RoomType roomType : roomTypes) {
            em.detach(roomType);
            for (RoomRate roomRate : roomType.getRoomRates()) {
                em.detach(roomRate);
                roomRate.setRoomType(null);
            }
        }

        return roomTypes;
    }
    
    @WebMethod
    public List<Room> searchAvailableRooms(Date checkInDate, Date checkOutDate, Long roomTypeId) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND r NOT IN (SELECT rr.room FROM ReservationRoom rr "
                + "WHERE rr.reservedFrom < :checkOutDate AND rr.reservedTo > :checkInDate)");
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);
        return query.getResultList();
    }
    
    @WebMethod
    public Reservation reserveRoom(Long partnerId, Long roomId, Date checkInDate, Date checkOutDate) throws GuestNotFoundException, RoomNotAvailableException, ReservationNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        Room room = em.find(Room.class, roomId);
        if (partner == null) {
            throw new GuestNotFoundException("Partner ID " + partnerId + " does not exist.");
        }
        if (room.getRoomStatus() != RoomStatusEnum.AVAILABLE) {
            throw new RoomNotAvailableException("Room ID " + roomId + " is not available.");
        }
        
        Reservation newReservation = new Reservation();
        newReservation.setPartner(partner);
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
        


        BigDecimal totalAmount = reservationAmt(room.getRoomType().getRoomTypeId(), checkInDate, checkOutDate); 
        newReservation.setReservationAmt(totalAmount);

        Calendar cal = Calendar.getInstance();
        if (checkInDate.equals(new Date()) && cal.get(Calendar.HOUR_OF_DAY) >= 2) {
            newReservation.setReservationStatus(ReservationStatus.CHECKEDIN); 
        } else {
            newReservation.setReservationStatus(ReservationStatus.RESERVED);
        }

        em.persist(newReservation);
        return newReservation;
    }
    
    @WebMethod
    public BigDecimal reservationAmt(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal roomRatePerNight = getApplicableRoomRate(roomTypeId, checkInDate, checkOutDate);

        long numNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);

        totalAmount = totalAmount.add(roomRatePerNight.multiply(new BigDecimal(numNights)));
        return totalAmount;
    }
    
    @WebMethod
    public BigDecimal getApplicableRoomRate(Long roomTypeId, Date checkInDate, Date checkOutDate) {
        List<RoomRate> roomRates = getRoomRatesForRoomType(roomTypeId, checkInDate, checkOutDate);

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

        if (promotionRate != null) {
            return promotionRate.getRatePerNight(); 
        } else if (peakRate != null) {
            return peakRate.getRatePerNight(); 
        } else if (normalRate != null) {
            return normalRate.getRatePerNight();
        } else {
            throw new IllegalStateException("No rate defined for room type " + roomTypeId + " on the selected dates.");
        }
    }
    
    @WebMethod
    public List<RoomRate> getRoomRatesForRoomType(Long roomTypeId, Date checkInDate, Date checkOutDate) {
    Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomType.roomTypeId = :roomTypeId "
            + "AND ((rr.validFrom IS NULL AND rr.validTo IS NULL) "
            + "OR (rr.validFrom <= :checkOutDate AND rr.validTo >= :checkInDate))");

    query.setParameter("roomTypeId", roomTypeId);
    query.setParameter("checkInDate", checkInDate);
    query.setParameter("checkOutDate", checkOutDate);

    return query.getResultList();
    }
    
    @WebMethod
    public Reservation getReservationDetails(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation with ID " + reservationId + " not found.");
        }
        return reservation;
    }
    
    @WebMethod
    public List<Reservation> getAllReservationsForPartner(Long partnerId) {
        TypedQuery<Reservation> query = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.partner.partnerId = :partnerId", Reservation.class);
        query.setParameter("partnerId", partnerId);
        return query.getResultList();
    }

}
