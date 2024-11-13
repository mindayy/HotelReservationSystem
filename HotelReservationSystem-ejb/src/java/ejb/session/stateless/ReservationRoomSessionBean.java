/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author kaixin
 */
@Stateless
public class ReservationRoomSessionBean implements ReservationRoomSessionBeanRemote, ReservationRoomSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

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
}
