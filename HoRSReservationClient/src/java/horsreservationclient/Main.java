/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author min
 */
public class Main {

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(roomTypeSessionBeanRemote);
        mainApp.runApp();
    }
    
}
