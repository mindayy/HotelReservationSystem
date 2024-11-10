/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import entity.ReserveRoom;

/**
 *
 * @author kaixin
 */
@Stateless
public class ExceptionSessionBean implements ExceptionSessionBeanRemote, ExceptionSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createRoomAllocationException(ReserveRoom reserveRoom, String exceptionType, String message) {
        // Exception exception = new Exception(message, reserveRoom, exceptionType);
        // em.persist(exception);
    }

    @Override
    public List<Exception> retrieveRoomAllocationExceptions() {
        Query query = em.createQuery("SELECT e FROM Exception e WHERE e.exceptionType IN ('UPGRADE_ROOM', 'NO_ROOM_AVAILABLE')");
        return query.getResultList();
    }
}
