/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enums.RateTypeEnum;
import enums.RoleEnum;
import enums.RoomStatusEnum;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.InvalidAccessRightException;
import util.exception.RoomRateNotFoundException;
import util.exception.RoomTypeNotFoundException;
import ejb.session.stateless.ExceptionReportSessionBeanRemote;
import ejb.session.stateless.ReservationRoomSessionBeanRemote;

/**
 *
 * @author min
 */
public class HotelOperationModule {
    
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    private ExceptionReportSessionBeanRemote exceptionReportSessionBeanRemote;
    private ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote;

    
    private Employee currentEmployee;

    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, ExceptionReportSessionBeanRemote exceptionReportSessionBeanRemote, ReservationRoomSessionBeanRemote reservationRoomSessionBeanRemote,  Employee currentEmployee) {
        this();
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.exceptionReportSessionBeanRemote = exceptionReportSessionBeanRemote;
        this.reservationRoomSessionBeanRemote = reservationRoomSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    

    void menuHotelOperation() throws InvalidAccessRightException {
        if (currentEmployee.getRole() == RoleEnum.OperationManager) {
            displayOperationManagerMenu();
        } else if (currentEmployee.getRole() == RoleEnum.SalesManager) {
            displaySalesManagerMenu();
        } else {
            throw new InvalidAccessRightException("You do not have the rights to access the Hotel Operation Module.");
        }
    }
    
    private void displayOperationManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Reservation System :: Hotel Operation (Operation Manager) ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("9: Allocate Rooms to Current Day Reservations"); 
            System.out.println("-----------------------");
            System.out.println("10: Back\n");
            response = 0;

            while (response < 1 || response > 10) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRoomType();
                } else if (response == 2) {
                    doViewRoomTypeDetails();
                } else if (response == 3) {
                    doViewAllRoomTypes();
                } else if (response == 4) {
                    doCreateNewRoom();
                } else if (response == 5) {
                    doUpdateRoom();
                } else if (response == 6) {
                    doDeleteRoom();
                } else if (response == 7) {
                    doViewAllRooms();
                } else if (response == 8) {
                    doViewAllRoomAllocationExceptionReport();
                } else if (response == 9) {
                    doAllocateRoomsForReservations();
                } else if (response == 10) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 10) {
                break;
            }
        }
    }   

    private void displaySalesManagerMenu() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Hotel Reservation System :: Hotel Operation (Sales Manager) ***\n");
            System.out.println("1: Create New Room Rate");
            System.out.println("2: View Room Rate Details");
            System.out.println("3: View All Room Rates");
            System.out.println("4: Back\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRoomRate();
                } else if (response == 2) {
                    doViewRoomRateDetails();
                } else if (response == 3) {
                    doViewAllRoomRates();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }  

    private void doCreateNewRoomType() {
        Scanner scanner = new Scanner(System.in);
        RoomType newRoomType = new RoomType();
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: Create New Room Type ***\n");
        System.out.print("Enter Room Type Name> ");
        newRoomType.setName(scanner.nextLine().trim());
        System.out.print("Enter Room Type Description> ");
        newRoomType.setDescription(scanner.nextLine().trim());
        System.out.print("Enter Room Type Size> ");
        String sizeInput = (scanner.nextLine().trim());
        int size = Integer.parseInt(sizeInput); // Convert String to int
        newRoomType.setSize(size);
        System.out.print("Enter Room Type Bed> ");
        newRoomType.setBed(scanner.nextLine().trim());
        System.out.print("Enter Room Type Capacity> ");
        String capacityInput = (scanner.nextLine().trim());
        int capacity = Integer.parseInt(capacityInput); // Convert String to int
        newRoomType.setCapacity(capacity);
        System.out.print("Enter Room Type Amenities> ");
        newRoomType.setAmenities(scanner.nextLine().trim());
        
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%-12s %-20s\n", "Room Type Id", "Name");
        for(RoomType existingRoomType:roomTypes)
        {
        System.out.printf("%-12s %-20s\n", existingRoomType.getRoomTypeId(), existingRoomType.getName());
        }
        System.out.printf("Enter 'N' if no Next Higher Room Type");
        System.out.print("Enter Next Higher Room Type Id> ");
        Long nextHigherRoomTypeId = scanner.nextLong();
        scanner.nextLine();
        RoomType nextHigherRoomType = null;
        try {
            nextHigherRoomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(nextHigherRoomTypeId);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
        }
        if (nextHigherRoomType != null) {
            newRoomType.setNextHigherRoomType(nextHigherRoomType);
        } else {
            System.out.println("Next Higher Room Type not set due to invalid ID.");
        }
        
        newRoomType.setIsDisabled(false);

        Long newRoomTypeId = roomTypeSessionBeanRemote.createNewRoomType(newRoomType);
        System.out.println("New room type created successfully!: " + newRoomTypeId + "\n");

    }

    private void doViewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Type Details ***\n");
        System.out.print("Enter Room Type ID> ");
        Long roomTypeId = scanner.nextLong();

        try {
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities", "Next Higher Room Type");
            
            // display null if no nexthigheroomtype
            String nextHigherRoomTypeName = (roomType.getNextHigherRoomType() != null) ? 
                roomType.getNextHigherRoomType().getName() : "None";
            
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s\n", roomType.getRoomTypeId(), roomType.getName(),
                    roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), 
                    roomType.getAmenities(), nextHigherRoomTypeName);
            
            System.out.println("------------------------");
            System.out.println("1: Update Room Type");
            System.out.println("2: Delete Room Type");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            
            if(response == 1)
            {
                doUpdateRoomType(roomType);
            }
            else if(response == 2)
            {
                doDeleteRoomType(roomType);
            }
        }
        catch(RoomTypeNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
        }
    }

    private void doUpdateRoomType(RoomType roomType) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Type Details :: Update Room Type ***\n");
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomType.setName(input);
        }
                
        System.out.print("Enter Description(blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomType.setDescription(input);
        }
        
        System.out.print("Enter Size (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            try {
                int size = Integer.parseInt(input);
                roomType.setSize(size);
            } catch (NumberFormatException e) {
                System.out.println("Invalid size input. Please enter a valid integer.");
            }
        }
        System.out.print("Enter Bed Type (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            roomType.setBed(input);
        }

        System.out.print("Enter Capacity (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            try {
                int capacity = Integer.parseInt(input);
                roomType.setCapacity(capacity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity input. Please enter a valid integer.");
            }
        }
        
        System.out.print("Enter Amenities (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            roomType.setAmenities(input);
        }
        
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%-12s %-20s\n", "Room Type Id", "Name");
        for(RoomType existingRoomType:roomTypes)
        {
        System.out.printf("%-12s %-20s\n", existingRoomType.getRoomTypeId(), existingRoomType.getName());
        }
        
        System.out.print("Enter Next Higher Room Type ID (blank if no change)> ");
        input = scanner.nextLine(); 
        RoomType nextHigherRoomType = null;

        if (input.trim().length() > 0) {
            try {
                Long roomTypeId = Long.parseLong(input); 
                nextHigherRoomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
            } 
        }
        
        roomType.setNextHigherRoomType(nextHigherRoomType);
        roomTypeSessionBeanRemote.updateRoomType(roomType);
        System.out.println("Room type updated successfully!\n");
        
    }

    private void doDeleteRoomType(RoomType roomType) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room type Details :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type: %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomType.getName(), roomType.getRoomTypeId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
 
            roomTypeSessionBeanRemote.deleteRoomType(roomType);
            System.out.println("Room Type deleted successfully!\n");

        }
        else
        {
            System.out.println("Room Type NOT deleted!\n");
        }
    }

    private void doViewAllRoomTypes() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View All Room Types ***\n");
        
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities",  "Next Higher Room Type");

        for(RoomType roomType:roomTypes)
        {   
            String nextHigherRoomTypeName = (roomType.getNextHigherRoomType() != null) ? 
                roomType.getNextHigherRoomType().getName() : "None";
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s %-10s \n", roomType.getRoomTypeId(), roomType.getName(),
                    roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities(), nextHigherRoomTypeName);
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewRoomRate() {
       Scanner scanner = new Scanner(System.in);
        RoomRate newRoomRate = new RoomRate();
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: Create New Room Rate ***\n");
        System.out.print("Enter Room Rate Name> ");
        newRoomRate.setRoomRateName(scanner.nextLine().trim());
        
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities");
        for (RoomType roomType:roomTypes)
        {
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", roomType.getRoomTypeId(), roomType.getName(),
                    roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
        }
        
        System.out.print("Enter Room Type Id> ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());

        System.out.print("Enter Room Rate Per Night> ");
        String rateInput = scanner.nextLine().trim();
        BigDecimal ratePerNight = new BigDecimal(rateInput);
        newRoomRate.setRatePerNight(ratePerNight);
        
        while(true)
        {
            System.out.print("Select Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion)> ");
            Integer rateTypeInt = scanner.nextInt();
            scanner.nextLine();
            
            if(rateTypeInt >= 1 && rateTypeInt <= 4)
            {
                newRoomRate.setRateType(RateTypeEnum.values()[rateTypeInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (newRoomRate.getRateType() == RateTypeEnum.PEAK || newRoomRate.getRateType() == RateTypeEnum.PROMOTION) {
           try {
               System.out.print("Enter Room Rate Start Date (yyyy-MM-dd) > ");
               String startDateInput = scanner.nextLine().trim();
               Date startDate = dateFormat.parse(startDateInput); // Convert String to Date
               newRoomRate.setValidFrom(startDate);
               System.out.print("Enter Room Rate End Date (yyyy-MM-dd) > ");
               String endDateInput = scanner.nextLine().trim();
               Date endDate = dateFormat.parse(endDateInput); // Convert String to Date
               newRoomRate.setValidTo(endDate);
           } catch (ParseException ex) {
               System.out.println("Please enter a valid Room Rate! \n");
           }
        }

        newRoomRate.setIsDisabled(false);
        Long newRoomRateId = roomRateSessionBeanRemote.createNewRoomRate(newRoomRate, roomTypeId);
        System.out.println("New room rate created successfully!: " + newRoomRateId + "\n");
    }

    private void doViewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String validFrom = "";
        String validTo = "";
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Rate Details ***\n");
        System.out.print("Enter Room Rate ID> ");
        Long roomRateId = scanner.nextLong();

        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);
            Long roomTypeId = roomRateSessionBeanRemote.getRoomTypeIdForRoomRate(roomRateId);
            if (roomRate.getRateType() == RateTypeEnum.PEAK || roomRate.getRateType() == RateTypeEnum.PROMOTION) {
                if (roomRate.getValidFrom() != null) {
                    validFrom = dateFormat.format(roomRate.getValidFrom());
                }
                if (roomRate.getValidTo() != null) {
                    validTo = dateFormat.format(roomRate.getValidTo());
                }
                System.out.printf("%-12s %-50s %-12s %-16s %-12s %-12s %-12s\n", "Room Rate Id", "Name",
                        "Rate Type", "Rate Per Night", "Valid From", "Valid To", "Room Type Id");
                System.out.printf("%-12s %-50s %-12s %-16s %-12s %-12s %-12s\n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), validFrom, validTo, roomTypeId);
            }
            else
            {
                System.out.printf("%-12s %-40s %-12s %-12s %-12s \n", "Room Rate Id", "Name",
                    "Rate Type", "Rate Per Night", "Room Type Id");
                System.out.printf("%-12s %-40s %-12s %-12s %-12s \n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                    roomRate.getRateType(), roomRate.getRatePerNight(), roomTypeId);
            }
            
            System.out.println("------------------------");
            System.out.println("1: Update Room Rate");
            System.out.println("2: Delete Room Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            
            if(response == 1)
            {
                doUpdateRoomRate(roomRate);
            }
            else if(response == 2)
            {
                doDeleteRoomRate(roomRate);
            }
        }
        catch(RoomRateNotFoundException ex)
        {
            System.out.println("An error has occurred while retrieving room rate: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllRoomRates() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View All Room Rates ***\n");
        
        List<RoomRate> roomRates = roomRateSessionBeanRemote.viewAllRoomRates();
        System.out.printf("%-12s %-50s %-12s %-16s %-12s %-12s %-12s\n", "Room Rate Id", "Name",
                        "Rate Type", "Rate Per Night", "Valid From", "Valid To", "Room Type Id");

        for(RoomRate roomRate : roomRates) {
            String validFrom = "N/A";
            String validTo = "N/A";

            if (roomRate.getRateType() == RateTypeEnum.PEAK || roomRate.getRateType() == RateTypeEnum.PROMOTION) {
                if (roomRate.getValidFrom() != null) {
                    validFrom = dateFormat.format(roomRate.getValidFrom());
                }
                if (roomRate.getValidTo() != null) {
                    validTo = dateFormat.format(roomRate.getValidTo());
                }
            }

            System.out.printf("%-12s %-50s %-12s %-16s %-12s %-12s %-12s\n", 
                              roomRate.getRoomRateId(), 
                              roomRate.getRoomRateName(),
                              roomRate.getRateType(),
                              roomRate.getRatePerNight(), 
                              validFrom, 
                              validTo, 
                              roomRate.getRoomType());
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateRoomRate(RoomRate roomRate) {
        Scanner scanner = new Scanner(System.in);        
        String input = null;
        Long roomTypeId = null;
        BigDecimal ratePerNight = null;
        RateTypeEnum rateType = null;
        String name = null;
        Date startDate = null;
        Date endDate = null;
        
        // name, rate type, rate per night, validfrom, validto, roomtypeid
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Rate Details :: Update Room Rate ***\n");
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            name = input;
        } else {
            name = roomRate.getRoomRateName();
        }
        
        
                
        System.out.print("Enter Rate Per Night(blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
        ratePerNight = new BigDecimal(input);
        } else {
            ratePerNight = roomRate.getRatePerNight();
        }

        
        System.out.print("Enter Room Type Id (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            roomTypeId = Long.parseLong(input);
            RoomType roomType;
            try {
                roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
            }
        } else {
            roomTypeId = roomRate.getRoomType().getRoomTypeId();
        }
        

        System.out.print("Enter Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion, blank if no change)> ");
        input = scanner.nextLine().trim();
        if (input.length() > 0) {
            try {
                Integer rateTypeInt = Integer.parseInt(input);
                rateType = RateTypeEnum.values()[rateTypeInt - 1];
            } catch (NumberFormatException ex) {
                System.out.println("Invalid option, please try again!");
            }
        } else {
            rateType = roomRate.getRateType();
        }
        
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (rateType == RateTypeEnum.PEAK || rateType == RateTypeEnum.PROMOTION) {
            try {
                System.out.print("Enter Room Rate Start Date (yyyy-MM-dd) (blank if no change)> ");
                String startDateInput = scanner.nextLine().trim();
                if(startDateInput.length() > 0) {
                    startDate = dateFormat.parse(startDateInput); // Convert String to Date
                } else {
                startDate = roomRate.getValidFrom();
                }
                System.out.print("Enter Room Rate End Date (yyyy-MM-dd) (blank if no change)> ");
                String endDateInput = scanner.nextLine().trim();
                if(endDateInput.length() > 0) {
                     endDate = dateFormat.parse(endDateInput); // Convert String to Date
                } else {
                endDate = roomRate.getValidTo();
                }
            } catch (ParseException ex) {
               System.out.println("Please enter a valid Date! \n");
            }
        }

        roomRateSessionBeanRemote.updateRoomRate(roomRate, name, roomTypeId, ratePerNight, rateType, startDate, endDate);
        System.out.println("Room rate updated successfully!\n");
    }

    private void doDeleteRoomRate(RoomRate roomRate) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Rate Details :: Delete Room Rate ***\n");
        System.out.printf("Confirm Delete Room Rate %s (Room Rate ID: %d) (Enter 'Y' to Delete)> ", roomRate.getRoomRateName(), roomRate.getRoomRateId());
        input = scanner.nextLine().trim();
        
        if(input.equals("Y"))
        {
 
            roomRateSessionBeanRemote.deleteRoomRate(roomRate);
            System.out.println("Room Rate deleted successfully!\n");

        }
        else
        {
            System.out.println("Room Rate NOT deleted!\n");
        }
    }
    
    private void doCreateNewRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: Create New Room ***\n");

        System.out.print("Enter Room Number> ");
        String roomNumber = sc.nextLine().trim();

        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes(); 

        // Display available room types
        System.out.println("Select Room Type:");
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            System.out.println((i + 1) + ": " + roomType.getName());
        }
        int roomTypeChoice = sc.nextInt();
        RoomType selectedRoomType = roomTypes.get(roomTypeChoice - 1); // Get selected RoomType

        System.out.println("Select Room Status:");
        for (RoomStatusEnum status : RoomStatusEnum.values()) {
            System.out.println((status.ordinal() + 1) + ": " + status);
        }
        int statusChoice = sc.nextInt();
        RoomStatusEnum selectedRoomStatus = RoomStatusEnum.values()[statusChoice - 1]; // Get selected RoomStatusEnum


        try {
            roomSessionBeanRemote.createRoom(roomNumber, selectedRoomType, selectedRoomStatus);
            System.out.println("New room created successfully!\n");
        } catch (Exception ex) {
            System.out.println("An error occurred while creating the room: " + ex.getMessage() + "\n");
        }
        
    }

    private void doUpdateRoom() {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: Update Room ***\n");

        // Retrieve all rooms 
        List<Room> rooms = roomSessionBeanRemote.retrieveAllRooms();

        // Display available rooms
        System.out.println("Select Room to Update:");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + ": Room " + room.getRoomNumber() + " - Type: " + room.getRoomType().getName() + ", Status: " + room.getRoomStatus());
        }
        int roomChoice = sc.nextInt();
        Room selectedRoom = rooms.get(roomChoice - 1); // Get selected room

        // Get the new room number
        sc.nextLine(); // consume newline
        System.out.print("Enter New Room Number (Leave blank to keep current)> ");
        String newRoomNumber = sc.nextLine().trim();
        if (!newRoomNumber.isEmpty()) {
            selectedRoom.setRoomNumber(newRoomNumber);
        }

        // Retrieve all room types from the database
        List<RoomType> roomTypes = roomTypeSessionBeanRemote.viewAllRoomTypes();
        System.out.println("Select New Room Type (Leave blank to keep current):");
        for (int i = 0; i < roomTypes.size(); i++) {
            RoomType roomType = roomTypes.get(i);
            System.out.println((i + 1) + ": " + roomType.getName());
        }
        int roomTypeChoice = sc.nextInt();
        if (roomTypeChoice != 0) {
            RoomType newRoomType = roomTypes.get(roomTypeChoice - 1);
            selectedRoom.setRoomType(newRoomType);
        }

        // Display available room status options
        System.out.println("Select New Room Status (Leave blank to keep current):");
        for (RoomStatusEnum status : RoomStatusEnum.values()) {
            System.out.println((status.ordinal() + 1) + ": " + status);
        }
        int statusChoice = sc.nextInt();
        if (statusChoice != 0) {
            RoomStatusEnum newStatus = RoomStatusEnum.values()[statusChoice - 1];
            selectedRoom.setRoomStatus(newStatus);
        }

        // Save the updated room
        try {
            roomSessionBeanRemote.updateRoom(selectedRoom);
            System.out.println("Room updated successfully!\n");
        } catch (Exception ex) {
            System.out.println("An error occurred while updating the room: " + ex.getMessage() + "\n");
        }
        
    }

    private void doDeleteRoom() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** HoRS Management System :: Delete Room ***\n");

        // Retrieve all rooms (assuming a method exists to fetch all rooms)
        List<Room> rooms = roomSessionBeanRemote.retrieveAllRooms();

        // Display available rooms
        System.out.println("Select Room to Delete:");
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            System.out.println((i + 1) + ": Room " + room.getRoomNumber() + " - Type: " + room.getRoomType().getName() + ", Status: " + room.getRoomStatus());
        }
        int roomChoice = sc.nextInt();
        Room selectedRoom = rooms.get(roomChoice - 1); // Get selected room

        // Confirm deletion
        System.out.print("Are you sure you want to delete Room " + selectedRoom.getRoomNumber() + "? (y/n): ");
        String confirmation = sc.next().trim();
        if (confirmation.equalsIgnoreCase("y")) {
            try {
                roomSessionBeanRemote.deleteRoom(selectedRoom.getRoomId());
                System.out.println("Room deleted successfully!\n");
            } catch (Exception ex) {
                System.out.println("An error occurred while deleting the room: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Room deletion cancelled.\n");
        }
    }

    private void doViewAllRooms() {
        System.out.println("*** HoRS Management System :: View All Rooms ***\n");

        List<Room> rooms = roomSessionBeanRemote.retrieveAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the system.\n");
        } else {
            System.out.println("List of All Rooms:");
            for (Room room : rooms) {
                System.out.println("Room " + room.getRoomNumber() + " - Type: " + room.getRoomType().getName() + ", Status: " + room.getRoomStatus());
            }
        }
    }

    private void doViewAllRoomAllocationExceptionReport() {
        System.out.println("*** Hotel Operation :: View Room Allocation Exception Report ***\n");

        try {
            // Call the session bean to generate the exception report
            List<String> exceptionReport = exceptionReportSessionBeanRemote.generateRoomAllocationExceptionReport();

            if (exceptionReport == null || exceptionReport.isEmpty()) {
                System.out.println("There are no room allocation exceptions at this time.\n");
            } else {
                System.out.println("Room Allocation Exception Report:\n");
                int index = 1;
                for (String reportEntry : exceptionReport) {
                    System.out.println(index + ". " + reportEntry); // Print each report entry with a number
                    index++;
                }
            }
        } catch (Exception ex) {
            System.out.println("An error occurred while retrieving the room allocation exception report: " + ex.getMessage() + "\n");
        }
    }   
    
    private void doAllocateRoomsForReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: Allocate Rooms for Current Day Reservations ***\n");

        // You may pass today's date or a specific date as per your business logic
        Date currentDate = new Date();  // Example: current date allocation
        try {
            reservationRoomSessionBeanRemote.allocateRoomsForDate(currentDate);  // Calling the session bean method
            System.out.println("Rooms allocated for the current day's reservations successfully.\n");
        } catch (Exception e) {
            System.out.println("An error occurred while allocating rooms: " + e.getMessage() + "\n");
        }
    }

}
