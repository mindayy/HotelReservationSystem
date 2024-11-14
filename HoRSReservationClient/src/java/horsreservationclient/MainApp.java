/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationRoomSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Customer;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author min
 */
class MainApp {
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote;
    
    private Customer currentGuest;

    public MainApp() {
    }


    MainApp(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, 
            ReservationSessionBeanRemote reservationSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote,
            GuestSessionBeanRemote guestSessionBeanRemote, ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote) {
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.guestSessionBeanRemote = guestSessionBeanRemote;
        this.reservationRoomSessionBeanRemote = reservationRoomSessionBeanRemote;
    }
    

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
         while(true)
        {
            System.out.println("*** Welcome to Hotel Reservation System ***\n");
            System.out.println("1: Guest Login");
            System.out.println("2: Register as Guest");
            System.out.println("3: Search Hotel Room");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    try
                    {
                        doLogin();
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex) 
                    {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }
                else if (response == 2)
                {
                    doRegistration();
                }
                else if (response ==3)
                {
                    doSearchHotelRoom();
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4)
            {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("*** Guest Login ***\n");
        System.out.print("Enter Username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();

        try {
            // Assuming there's a method in your session bean to authenticate a guest by email and password
            currentGuest = guestSessionBeanRemote.guestLogin(username, password);
            System.out.println("Login successful! Welcome, " + currentGuest.getUsername() + ".\n");
        } catch (InvalidLoginCredentialException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential: " + ex.getMessage());
        }
    
    }


    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View My Reservation Details");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Exit");
            System.out.print("> ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    doSearchHotelRoom();
                    break;
                case 2:
                    viewMyReservationDetails();
                    break;
                case 3:
                    viewAllMyReservations();
                    break;
                case 4:
                    return; // Exit guest menu
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void doRegistration() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Hotel Reservation System :: Register as Guest ***\n");
        System.out.print("Enter Email> ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();

   
        Long newGuestId = guestSessionBeanRemote.registerGuest(email, username, password);
        System.out.println("Registration successful! Your guest ID is " + newGuestId + ".\n");
    }

    private void doSearchHotelRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: Search Hotel Room ***\n");

        System.out.print("Enter Check-In Date (yyyy-mm-dd)> ");
        String checkInDateStr = scanner.nextLine().trim();
        System.out.print("Enter Check-Out Date (yyyy-mm-dd)> ");
        String checkOutDateStr = scanner.nextLine().trim();

        Date checkInDate = Date.valueOf(checkInDateStr);
        Date checkOutDate = Date.valueOf(checkOutDateStr);
        
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities",  "Next Higher Room Type");

        for(RoomType roomType:roomTypes)
        {   
            String nextHigherRoomTypeName = (roomType.getNextHigherRoomType() != null) ? 
                roomType.getNextHigherRoomType().getName() : "None";
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s \n", roomType.getRoomTypeId(), roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities(), nextHigherRoomTypeName);
        }
        
        System.out.print("Enter Room Type ID> ");
        Long roomTypeId = scanner.nextLong();
                    

        // Use the roomSessionBeanRemote or roomTypeSessionBeanRemote to search available rooms
        List<Room> availableRooms = reservationRoomSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate, roomTypeId);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for the specified dates.\n");
        } else {
            System.out.println("Available Rooms: ");
            for (Room room : availableRooms) {
                System.out.println("Room ID: " + room.getRoomId() + "Room Number: " + room.getRoomNumber() + ", Room Type: " + room.getRoomType().getName());
            }
            BigDecimal reservationAmt = reservationSessionBeanRemote.reservationAmt(roomTypeId, checkInDate, checkOutDate);
            System.out.println("Reservation Amount: $" + reservationAmt);
            
        }
        
    }

    private void viewMyReservationDetails() {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: View Reservation Details ***\n");

        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();

        try {
            // Retrieve reservation details based on the reservation ID
            Reservation reservation = reservationSessionBeanRemote.getReservationDetails(reservationId);

            if (reservation == null) {
                System.out.println("Reservation with ID " + reservationId + " not found.\n");
            } else {
                System.out.println("Reservation Details: ");
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Check-In Date: " + reservation.getCheckInDate());
                System.out.println("Check-Out Date: " + reservation.getCheckOutDate());
                System.out.println("Room Type: " + reservation.getRoomType().getName());
                System.out.println("Total Amount: $" + reservation.getReservationAmt());
            }

        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving the reservation details: " + ex.getMessage() + "\n");
        }
    }

    private void viewAllMyReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: View All My Reservations ***\n");

        try {
            // Assume customerId is available from the logged-in customer
            Long customerId = currentGuest.getGuestId(); 

            // Retrieve all reservations for the logged-in customer
            List<Reservation> reservations = reservationSessionBeanRemote.getAllReservationsForCustomer(customerId);

            if (reservations.isEmpty()) {
                System.out.println("You have no reservations.\n");
            } else {
                System.out.printf("%-10s %-20s %-20s %-20s %-10s\n", "Reservation ID", "Check-In Date", "Check-Out Date", "Room Type", "Total Amount");

                for (Reservation reservation : reservations) {
                    // Display basic reservation information
                    System.out.printf("%-10s %-20s %-20s %-20s %-10s\n",
                            reservation.getReservationId(),
                            reservation.getCheckInDate().toString(),
                            reservation.getCheckOutDate().toString(),
                            reservation.getRoomType().getName(),
                            reservation.getReservationAmt());
                }
            }

        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving your reservations: " + ex.getMessage() + "\n");
        }
    }

}
