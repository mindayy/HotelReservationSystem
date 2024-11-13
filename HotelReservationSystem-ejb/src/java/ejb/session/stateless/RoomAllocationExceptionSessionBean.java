/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationRoom;
import entity.RoomAllocationException;
import enums.RoomAllocationExceptionTypeEnum;
import java.util.ArrayList;
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
    public void createRoomAllocationException(ReservationRoom reservationRoom, RoomAllocationExceptionTypeEnum exceptionType, String message) {
        RoomAllocationException exception = new RoomAllocationException(message, reservationRoom, exceptionType);
        em.persist(exception);
    }

    // Method to retrieve a RoomAllocationException by its ID
    @Override
    public RoomAllocationException retrieveRoomAllocationExceptionById(Long exceptionId) {
        return em.find(RoomAllocationException.class, exceptionId);
    }

    // Method to retrieve all RoomAllocationExceptions (for reports or admin viewing)
    @Override
    public List<RoomAllocationException> retrieveAllRoomAllocationExceptions() {
        return em.createQuery("SELECT r FROM RoomAllocationException r", RoomAllocationException.class).getResultList();
    }
    
    // Method to generate an exception report with detailed messages
    @Override
    public List<String> generateRoomAllocationExceptionReport() {
        List<RoomAllocationException> exceptions = retrieveAllRoomAllocationExceptions();
        List<String> report = new ArrayList<>();
        for (RoomAllocationException exception : exceptions) {
            String reportEntry = "Reservation ID: " + exception.getReservationRoom().getReservationRoomId() 
                    + " | Message: " + exception.getMessage() 
                    + " | Exception Type: " + exception.getExceptionType();
            report.add(reportEntry);
        }
        return report;
    }
    
}
