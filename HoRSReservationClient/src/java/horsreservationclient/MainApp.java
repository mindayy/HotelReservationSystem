/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.RoomTypeSessionBeanRemote;
import java.util.Scanner;

/**
 *
 * @author min
 */
class MainApp {
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;

    public MainApp() {
    }

    public MainApp(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote) {
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
    }

    

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            
        }
    }
    
}
