/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationRoom;
import entity.ExceptionReport;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class ExceptionReportSessionBean implements ExceptionReportSessionBeanRemote, ExceptionReportSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public void logException(Long reservationRoomId, String errorMessage) {
        ReservationRoom reservationRoom = em.find(ReservationRoom.class, reservationRoomId);
        
        if (reservationRoom != null) {
            ExceptionReport exception = new ExceptionReport(errorMessage, reservationRoom);
            em.persist(exception);

            List<ExceptionReport> exceptions = reservationRoom.getExceptions();
            exceptions.add(exception);
            reservationRoom.setExceptions(exceptions);
        }
    }
    
    @Override
    public List<String> generateRoomAllocationExceptionReport() {
        List<ExceptionReport> exceptionReports = em.createQuery("SELECT e FROM ExceptionReport e", ExceptionReport.class).getResultList();
        List<String> reportStrings = new ArrayList<>();

        for (ExceptionReport report : exceptionReports) {
            String reportEntry = String.format("Exception ID: %d, ReservationRoom ID: %d, Error: %s, Date: %s",
                    report.getExceptionId(),
                    report.getReservationRoom().getReservationRoomId(),
                    report.getErrorMessage());
            reportStrings.add(reportEntry);
        }

        return reportStrings;
    }


}
