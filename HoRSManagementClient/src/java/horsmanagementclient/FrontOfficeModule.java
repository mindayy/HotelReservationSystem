/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationRoomSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Guest;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import enums.RoleEnum;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.GuestNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author min
 */
public class FrontOfficeModule {
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote;
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private GuestSessionBeanRemote guestSessionBeanRemote;
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
    Scanner scanner = new Scanner(System.in);
    System.out.println("*** Hotel Reservation System :: Walk-in Search Room ***\n");

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
        } catch (IllegalArgumentException e)          {
            System.out.println("Invalid date format. Please use yyyy-mm-dd format.");
        }
    }

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

    // Use the reservationRoomSessionBeanRemote to search for available rooms
    List<Room> availableRooms = reservationRoomSessionBeanRemote.searchAvailableRooms(checkInDate, checkOutDate, roomTypeId);

    if (availableRooms.isEmpty()) {
        System.out.println("No rooms available for the specified dates.\n");
    } else {
        System.out.println("Available Rooms: ");
        for (Room room : availableRooms) {
            System.out.println("Room ID: " + room.getRoomId() + " Room Number: " + room.getRoomNumber() + ", Room Type: " + room.getRoomType().getName());
        }
        BigDecimal reservationAmt = reservationSessionBeanRemote.reservationAmt(roomTypeId, checkInDate, checkOutDate);
        System.out.println("Reservation Amount: $" + reservationAmt);
    }

    System.out.println("------------------------");
    System.out.println("1: Reserve Hotel Room");
    System.out.println("2: Back\n");
    System.out.print("> ");
    int response = scanner.nextInt();

    if (response == 1) {
        doWalkInReserveRoom(checkInDate, checkOutDate, roomTypeId, availableRooms);
    }
 }
    
    private void doWalkInReserveRoom(Date checkInDate, Date checkOutDate, Long roomTypeId, List<Room> availableRooms) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Reserving a Room for Walk-in Guest ***\n");

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

        // Proceed with the reservation
        try {
            // Step 1: Create a walk-in guest
            Guest walkInGuest = new Guest();
            walkInGuest.setEmail("walkin@guest.com");

            // Use session bean to persist the walk-in guest
            Guest newWalkInGuest = guestSessionBeanRemote.createNewGuest(walkInGuest);
            Long walkInGuestId = newWalkInGuest.getGuestId(); // Retrieve the ID of the newly created guest

            // Step 2: Proceed with the reservation
            Reservation newReservation = reservationSessionBeanRemote.reserveRoom(
                walkInGuestId, // Pass the new walk-in guest's ID
                roomId, 
                checkInDate, 
                checkOutDate
            );

            System.out.println("Reservation successful! Reservation ID: " + newReservation.getReservationId());
            System.out.println("Check-In Date: " + checkInDate);
            System.out.println("Check-Out Date: " + checkOutDate);
            System.out.println("Reserved for Walk-In Customer (Guest ID: " + walkInGuestId + ")");

        } catch (GuestNotFoundException e) {
            System.out.println("Error: Guest not found. Please verify the guest details.");
        } catch (ReservationNotFoundException e) {
            System.out.println("Error: Reservation not found. Please check the reservation details.");
        } catch (RoomNotAvailableException e) {
            System.out.println("Error: The room is not available for the selected dates. Please choose a different room.");
        }

    }

}
