/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.ExceptionReportSessionBeanRemote;
import ejb.session.stateless.ReservationRoomSessionBeanRemote;

/**
 *
 * @author min
 */
public class Main {

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static ExceptionReportSessionBeanRemote roomAllocationExceptionSessionBeanRemote;
    @EJB
    private static ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote;
    
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(roomTypeSessionBeanRemote, roomSessionBeanRemote, roomRateSessionBeanRemote, 
                   reservationSessionBeanRemote, employeeSessionBeanRemote, partnerSessionBeanRemote, roomAllocationExceptionSessionBeanRemote,reservationRoomSessionBeanRemote);//insert session beans
        mainApp.runApp();
    }
    
}
