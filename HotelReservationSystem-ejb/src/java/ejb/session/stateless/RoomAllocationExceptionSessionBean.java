/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationRoom;
import entity.RoomAllocationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class RoomAllocationExceptionSessionBean implements RoomAllocationExceptionSessionBeanRemote, RoomAllocationExceptionSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Create a new RoomAllocationException
    @Override
    public void createRoomAllocationException(ReservationRoom reservationRoom, String exceptionType, String message) {
        RoomAllocationException exception = new RoomAllocationException(message, reservationRoom, exceptionType);
        em.persist(exception);
    }

    // Retrieve all Room Allocation Exceptions
    @Override
    public List<RoomAllocationException> retrieveRoomAllocationExceptions() {
        Query query = em.createQuery("SELECT e FROM RoomAllocationException e WHERE e.exceptionType IN ('UPGRADE_ROOM', 'NO_ROOM_AVAILABLE')");
        return query.getResultList();
    }
}
