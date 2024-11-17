/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.RoomAllocationException;
import util.exception.RoomNotAvailableException;
import enums.ReservationStatus;
import javax.ejb.EJB;

/**
 *
 * @author kaixin
 */
@Stateless
public class ReservationRoomSessionBean implements ReservationRoomSessionBeanRemote, ReservationRoomSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private ExceptionReportSessionBean exceptionReportSessionBean;

    @Override
    public List<Room> searchAvailableRooms(Date checkInDate, Date checkOutDate, Long roomTypeId) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.roomType.roomTypeId = :roomTypeId "
                + "AND r NOT IN (SELECT rr.room FROM ReservationRoom rr "
                + "WHERE rr.reservedFrom < :checkOutDate AND rr.reservedTo > :checkInDate)");
        query.setParameter("roomTypeId", roomTypeId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);
        return query.getResultList();
    }

    @Override
    public ReservationRoom reserveRoom(Long roomId, Reservation reservation, Date checkInDate, Date checkOutDate) throws RoomNotAvailableException, ReservationNotFoundException {
        Room room = em.find(Room.class, roomId);
        if (room == null || reservation == null) {
            throw new ReservationNotFoundException("Room or Reservation not found.");
        }

        // Check if the room is available
        Query query = em.createQuery("SELECT rr FROM ReservationRoom rr WHERE rr.room.roomId = :roomId "
                + "AND rr.reservedFrom < :checkOutDate AND rr.reservedTo > :checkInDate");
        query.setParameter("roomId", roomId);
        query.setParameter("checkInDate", checkInDate);
        query.setParameter("checkOutDate", checkOutDate);

        if (!query.getResultList().isEmpty()) {
            throw new RoomNotAvailableException("Room is not available for the specified dates.");
        }

        // Create a new ReservationRoom entry
        ReservationRoom reservationRoom = new ReservationRoom(room, reservation, checkInDate, checkOutDate);
        em.persist(reservationRoom);
        return reservationRoom;
    }
    
    @Override
    public void allocateRoom(Long reservationRoomId, Long roomTypeId) throws RoomAllocationException {
        ReservationRoom reservationRoom = em.find(ReservationRoom.class, reservationRoomId);

        if (reservationRoom == null) {
            throw new RoomAllocationException("ReservationRoom ID " + reservationRoomId + " does not exist!");
        }

        List<Room> availableRooms = searchAvailableRooms(
            reservationRoom.getReservedFrom(),
            reservationRoom.getReservedTo(),
            roomTypeId
        );

        if (availableRooms.isEmpty()) {
            throw new RoomAllocationException("No available rooms of type " + roomTypeId 
                + " for ReservationRoom ID " + reservationRoomId);
        }

        Room allocatedRoom = availableRooms.get(0);
        reservationRoom.setRoom(allocatedRoom);
        reservationRoom.setRoomAllocated(true);

        em.merge(reservationRoom);
        em.merge(allocatedRoom);
    }
    
    @Override
    @Schedule(hour = "2", minute = "0", persistent = false) // Timer for 2 AM
    public void allocateRoomsForToday() {
        allocateRoomsForDate(new Date());
    }

    @Override
    public void allocateRoomsForDate(Date date) {
        List<Reservation> reservationsForDate = em.createQuery(
            "SELECT r FROM Reservation r WHERE r.checkInDate = :date AND r.reservationStatus = :reservedStatus", Reservation.class)
            .setParameter("date", date)
            .setParameter("reservedStatus", ReservationStatus.RESERVED)
            .getResultList();

        for (Reservation reservation : reservationsForDate) {
            try {
                // Find an available room based on the room type
                Room availableRoom = findAvailableRoom(reservation.getRoomType());
                if (availableRoom != null) {
                    // Allocate room to reservation
                    allocateRoomToReservation(reservation, availableRoom, date);
                } else {
                    throw new RoomAllocationException("No available room for reservation ID " + reservation.getReservationId());
                }
            } catch (RoomAllocationException e) {
                // Log exception using exception reporting service
                exceptionReportSessionBean.logException(reservation.getReservationId(), e.getMessage());
            }
        }
    }
    
    // Find an available room based on the room type
    private Room findAvailableRoom(RoomType roomType) {
        List<Room> availableRooms = em.createQuery(
            "SELECT r FROM Room r WHERE r.roomType = :roomType AND r.isAvailable = true", Room.class)
            .setParameter("roomType", roomType)
            .getResultList();
        
        return availableRooms.isEmpty() ? null : availableRooms.get(0); // Return the first available room
    }

    // Allocate room to reservation
    private void allocateRoomToReservation(Reservation reservation, Room availableRoom, Date date) {
        ReservationRoom reservationRoom = new ReservationRoom();
        reservationRoom.setRoom(availableRoom);
        reservationRoom.setReservedFrom(date);
        reservationRoom.setReservedTo(reservation.getCheckOutDate());
        reservationRoom.setReservation(reservation);
        em.persist(reservationRoom);

        // Update reservation status and add reservation room
        reservation.getReservationRooms().add(reservationRoom);
        reservation.setReservationStatus(ReservationStatus.ALLOCATED);
        em.merge(reservation);
    }
}

