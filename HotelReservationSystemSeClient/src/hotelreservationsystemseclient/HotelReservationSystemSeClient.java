/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemseclient;

import ws.partner.PartnerWebService_Service;

/**
 *
 * @author min
 */
public class HotelReservationSystemSeClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PartnerWebService_Service service = new PartnerWebService_Service();
        MainApp mainApp = new MainApp(service);
        mainApp.runApp();
    }
    
}
