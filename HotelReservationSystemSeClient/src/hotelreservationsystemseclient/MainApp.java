/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemseclient;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.partner.GuestNotFoundException;
import ws.partner.GuestNotFoundException_Exception;
import ws.partner.InvalidLoginCredentialException;
import ws.partner.InvalidLoginCredentialException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerWebService_Service;
import ws.partner.Reservation;
import ws.partner.ReservationNotFoundException;
import ws.partner.ReservationNotFoundException_Exception;
import ws.partner.Room;
import ws.partner.RoomNotAvailableException;
import ws.partner.RoomNotAvailableException_Exception;
import ws.partner.RoomType;

/**
 *
 * @author min
 */
class MainApp {
    
    private Partner currentPartner;
    private final PartnerWebService_Service service;

    MainApp(PartnerWebService_Service service) {
        this.service = service;
    }

    void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
         while(true)
        {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Search Hotel Room");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doLogin();
                    menuMain();
                }
                else if (response == 2)
                {
                    doSearchHotelRoom();
                } else if (response == 3)
                {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Partner Login ***\n");
        System.out.print("Enter Username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();

        try {
            currentPartner = service.getPartnerWebServicePort().partnerLogin(username, password);  
            System.out.println("Login successful! Welcome, " + currentPartner.getUsername() + ".\n");
        } catch (InvalidLoginCredentialException_Exception ex) {
            System.out.println("Login failed: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer choice;

        while (true) {
            System.out.println("1: Search Hotel Room");
            System.out.println("2: View Partner Reservation Details");
            System.out.println("3: View All Partner Reservations");
            System.out.println("4: Exit");

            choice = 0;  // Reset choice for each loop iteration
            while (choice < 1 || choice > 4) {
                System.out.print("> ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Please enter a valid number.");
                    scanner.next();  // Clear the invalid input
                    continue;
                }

                if (choice == 1) {
                    doSearchHotelRoom();
                } else if (choice == 2) {
                    viewMyReservationDetails();
                } else if (choice == 3) {
                    viewAllMyReservations();
                } else if (choice == 4) {
                    System.out.println("Exiting...");
                    break;  // Exit the menuMain method
                } else {
                    System.out.println("Invalid option, please try again.");
                }
            }
            if (choice == 4) {
                break;
            }
        }
    
    }

    private void doSearchHotelRoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Holiday Reservation System :: Search Hotel Room ***\n");

        String checkInDateStr, checkOutDateStr;
        Date checkInDate = null, checkOutDate = null;
    
        // Validate check-in date
        while (checkInDate == null) {
            System.out.print("Enter Check-In Date (yyyy-mm-dd)> ");
            checkInDateStr = scanner.nextLine().trim();
            try {
                checkInDate = Date.valueOf(checkInDateStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd format.");
            }
        }

        // Validate check-out date
        while (checkOutDate == null) {
            System.out.print("Enter Check-Out Date (yyyy-mm-dd)> ");
            checkOutDateStr = scanner.nextLine().trim();
            try {
                checkOutDate = Date.valueOf(checkOutDateStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd format.");
            }
        }

        try {
            // Convert Date to XMLGregorianCalendar
            XMLGregorianCalendar xmlCheckInDate = convertToXMLGregorianCalendar(checkInDate);
            XMLGregorianCalendar xmlCheckOutDate = convertToXMLGregorianCalendar(checkOutDate);

            // Fetch available room types
            List<RoomType> roomTypes = service.getPartnerWebServicePort().viewAllRoomTypes();
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s\n", "Room Type Id", "Name",
                        "Description", "Size", "Bed", "Capacity", "Amenities",  "Next Higher Room Type");

            for(RoomType roomType : roomTypes) {   
                String nextHigherRoomTypeName = (roomType.getNextHigherRoomType() != null) ? 
                    roomType.getNextHigherRoomType().getName() : "None";
                System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s \n", roomType.getRoomTypeId(),
                        roomType.getName(), roomType.getDescription(), roomType.getSize(), roomType.getBed(),
                        roomType.getCapacity(), roomType.getAmenities(), nextHigherRoomTypeName);
            }

            // Get Room Type ID input
            Long roomTypeId = null;
            while (roomTypeId == null) {
                System.out.print("Enter Room Type ID> ");
                try {
                    roomTypeId = scanner.nextLong();
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid Room Type ID.");
                    scanner.nextLine(); // Clear the buffer
                }
            }

            // Fetch available rooms
            List<Room> availableRooms = service.getPartnerWebServicePort().searchAvailableRooms(xmlCheckInDate, xmlCheckOutDate, roomTypeId);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available for the specified dates.\n");
            } else {
                System.out.println("Available Rooms: ");
                for (Room room : availableRooms) {
                    System.out.println("Room ID: " + room.getRoomId() + " Room Number: " + room.getRoomNumber() + ", Room Type: " + room.getRoomType().getName());
                }

                // Fetch reservation amount
                BigDecimal reservationAmt = service.getPartnerWebServicePort().reservationAmt(roomTypeId, xmlCheckInDate, xmlCheckOutDate);
                System.out.println("Reservation Amount: $" + reservationAmt);
            }

            System.out.println("------------------------");
            System.out.println("1: Reserve Hotel Room");
            System.out.println("2: Back\n");
            System.out.print("> ");
            int response = scanner.nextInt();

            if (response == 1) {   
                if (currentPartner != null && currentPartner.isLoggedIn()) {
                    doReserveRoom(checkInDate, checkOutDate, roomTypeId, availableRooms);
                } else {
                    System.out.println("You must be logged in to reserve a hotel room.");
                }
            } 
        } catch (Exception e) {
            System.out.println("Error processing the search: " + e.getMessage());
        }
    
    }
    
    private XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) throws Exception {
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        return datatypeFactory.newXMLGregorianCalendar((GregorianCalendar) GregorianCalendar.getInstance());
    }

    private void viewMyReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Holiday Reservation System :: View Reservation Details ***\n");

        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();

        try {
            // Retrieve reservation details based on the reservation ID
            Reservation reservation = service.getPartnerWebServicePort().getReservationDetails(reservationId);

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
        System.out.println("*** Holiday Reservation System :: View All My Reservations ***\n");

        try {
            // Assume customerId is available from the logged-in customer
            Long partnerId = currentPartner.getPartnerId(); 

            // Retrieve all reservations for the logged-in customer
            List<Reservation> reservations = service.getPartnerWebServicePort().getAllReservationsForPartner(partnerId);

            if (reservations.isEmpty()) {
                System.out.println("You have no reservations.\n");
            } else {
                System.out.printf("%-10s %-40s %-40s %-20s %-10s\n", "Reservation ID", "Check-In Date", "Check-Out Date", "Room Type", "Total Amount");

                for (Reservation reservation : reservations) {
                    // Display basic reservation information
                    System.out.printf("%-10s %-40s %-40s %-20s %-10s\n",
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

    private void doReserveRoom(Date checkInDate, Date checkOutDate, Long roomTypeId, List<Room> availableRooms) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Reserving a room ***\n");

        System.out.println("Select Room ID from the available rooms: ");
        for (Room room : availableRooms) {
            System.out.println("Room ID: " + room.getRoomId() + " - Room Number: " + room.getRoomNumber());
        }

        System.out.print("Enter Room ID to reserve> ");
        Long roomId = scanner.nextLong();

        // Check if the selected room ID is valid
        boolean roomExists = availableRooms.stream().anyMatch(room -> room.getRoomId().equals(roomId));
        if (!roomExists) {
            System.out.println("Invalid Room ID. Please try again.");
            return;
        }
        
        XMLGregorianCalendar xmlCheckInDate = null;
        XMLGregorianCalendar xmlCheckOutDate = null;

        try {
            xmlCheckInDate = convertToXMLGregorianCalendar(checkInDate);
            xmlCheckOutDate = convertToXMLGregorianCalendar(checkOutDate);
        } catch (Exception e) {
            System.out.println("Error converting dates: " + e.getMessage());
            return; 
        }

        if (xmlCheckInDate == null || xmlCheckOutDate == null) {
            System.out.println("Error converting dates. Reservation failed.");
            return;
        }
        
        Reservation newReservation = null;
        // Proceed with the reservation using the session bean
        try {
            newReservation = service.getPartnerWebServicePort().reserveRoom(
                    currentPartner.getPartnerId(),
                    roomId,
                    xmlCheckInDate,
                    xmlCheckOutDate
            );
        } catch (GuestNotFoundException_Exception ex) {
            System.out.println("Error: Guest not found. Please verify the guest details.");
        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println("Error: Reservation not found. Please check the reservation details.");
        } catch (RoomNotAvailableException_Exception ex) {
            System.out.println("Error: The room is not available for the selected dates. Please choose a different room.");
        }

        System.out.println("Reservation successful! Reservation ID: " + newReservation.getReservationId());
        System.out.println("Check-In Date: " + checkInDate);
        System.out.println("Check-Out Date: " + checkOutDate);
    }
    
    
    
}
