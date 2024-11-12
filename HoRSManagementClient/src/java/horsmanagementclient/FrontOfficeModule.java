/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Employee;
import enums.RoleEnum;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomAllocationException;

/**
 *
 * @author min
 */
public class FrontOfficeModule {
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    private Employee currentEmployee;

    public FrontOfficeModule() {
    }

    FrontOfficeModule(ReservationSessionBeanRemote reservationSessionBeanRemote, Employee currentEmployee) {
        this();
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    void menuFrontOffice() throws InvalidAccessRightException {
        if (currentEmployee.getRole() != RoleEnum.GuestRelationOfficer) {
            throw new InvalidAccessRightException("You do not have the rights to access the Front Office Module.");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Reservation System :: Front Office Module ***\n");
            System.out.println("1: Check-In Guest");
            System.out.println("2: Check-Out Guest");
            System.out.println("3: Walk-in Search Room");
            System.out.println("4: Back\n");
            response = 0;

            while(response < 1 || response > 12)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCheckInGuest();
                } else if (response == 2) {
                    doCheckOutGuest();
                } else if (response == 3) {
                    doWalkInSearchRoom();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again.\n");
                }
            }
            if(response == 4)
            {
                break;
            }
        }
    }

    private void doCheckInGuest() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Hotel Reservation System :: Front Office Module :: Check-In Guest ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        scanner.nextLine(); // Clear buffer

        try {
            reservationSessionBeanRemote.checkInGuest(reservationId);
            System.out.println("Guest successfully checked in!\n");
        } catch (ReservationNotFoundException ex) {
            System.out.println("An error occurred: " + ex.getMessage() + "\n");
        } catch (RoomAllocationException ex) {
            System.out.println("Room allocation issue: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage() + "\n");
        }
    }

    private void doCheckOutGuest() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Hotel Reservation System :: Front Office Module :: Check-Out Guest ***\n");
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        scanner.nextLine(); // Clear buffer

        try {
            reservationSessionBeanRemote.checkOutGuest(reservationId);
            System.out.println("Guest successfully checked out!\n");
        } catch (ReservationNotFoundException ex) {
            System.out.println("An error occurred: " + ex.getMessage() + "\n");
        } catch (IllegalStateException ex) {
            System.out.println("Invalid operation: " + ex.getMessage() + "\n");
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage() + "\n");

        }
    }

    private void doWalkInSearchRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
