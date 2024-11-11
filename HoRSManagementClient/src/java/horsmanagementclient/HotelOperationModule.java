/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.RoomRate;
import entity.RoomType;
import enums.RateTypeEnum;
import enums.RoleEnum;
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

/**
 *
 * @author min
 */
public class HotelOperationModule {
    
    private RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;
    private RoomSessionBeanRemote roomSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    private Employee currentEmployee;

    public HotelOperationModule() {
    }

    public HotelOperationModule(RoomTypeSessionBeanRemote roomTypeSessionBeanRemote, RoomSessionBeanRemote roomSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote, Employee currentEmployee) {
        this();
        this.roomTypeSessionBeanRemote = roomTypeSessionBeanRemote;
        this.roomSessionBeanRemote = roomSessionBeanRemote;
        this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    

    void menuHotelOperation() throws InvalidAccessRightException {
        if (currentEmployee.getRole() != RoleEnum.OperationManager && currentEmployee.getRole() != RoleEnum.SalesManager) {
            throw new InvalidAccessRightException("You do not have the rights to access the Hotel Operation Module.");
        }
    
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** Hotel Reservation System :: Hotel Operation ***\n");
            System.out.println("1: Create New Room Type");
            System.out.println("2: View Room Type Details");
            System.out.println("3: View All Room Types");
            System.out.println("-----------------------");
            System.out.println("4: Create New Room");
            System.out.println("5: Update Room");
            System.out.println("6: Delete Room");
            System.out.println("7: View All Rooms");
            System.out.println("8: View Room Allocation Exception Report");
            System.out.println("-----------------------");
            System.out.println("9: Create New Room Rate");
            System.out.println("10: View Room Rate Details");
            System.out.println("11: View All Room Rates");
            System.out.println("-----------------------");
            System.out.println("12: Back\n");
            response = 0;
            
            while(response < 1 || response > 12)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewRoomType();
                }
                else if(response == 2)
                {
                    doViewRoomTypeDetails();
                }
                else if(response == 3)
                {
                    doViewAllRoomTypes();
                }
                else if(response == 4)
                {
                    doCreateNewRoom();
                }
                else if (response == 5)
                {
                    doUpdateRoom();
                }
                else if(response == 6)
                {
                    doDeleteRoom();
                }
                else if(response == 7)
                {
                    doViewAllRooms();
                }
                else if(response == 18)
                {
                    doViewAllRoomAllocationExceptionReport();
                }
                else if(response == 9)
                {
                    doCreateNewRoomRate();
                }
                else if(response == 10)
                {
                    doViewRoomRateDetails();
                }
                else if(response == 11)
                {
                    doViewAllRoomRates();
                }
                else if(response == 12)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 12)
            {
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
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities");
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", roomType.getRoomTypeId(), roomType.getName(),
                    roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
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
        roomTypeSessionBeanRemote.updateRoomType(roomType);
        System.out.println("Room type updated successfully!\n");
    }

    private void doDeleteRoomType(RoomType roomType) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room type Details :: Delete Room Type ***\n");
        System.out.printf("Confirm Delete Room Type %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomType.getName(), roomType.getRoomTypeId());
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
        System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities");

        for(RoomType roomType:roomTypes)
        {
            System.out.printf("%-12s %-20s %-30s %-10s %-12s %-10s %-30s\n", roomType.getRoomTypeId(), roomType.getName(),
                    roomType.getDescription(), roomType.getSize(), roomType.getBed(), roomType.getCapacity(), roomType.getAmenities());
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
        System.out.print("Enter Room Type Id> ");
        Long roomTypeId = Long.parseLong(scanner.nextLine().trim());
        RoomType roomType;
        try {
            roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            newRoomRate.setRoomType(roomType);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
        }
        System.out.print("Enter Room Rate Per Night> ");
        String rateInput = scanner.nextLine().trim();
        BigDecimal ratePerNight = new BigDecimal(rateInput);
        newRoomRate.setRatePerNight(ratePerNight);
        
        while(true)
        {
            System.out.print("Select Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion)> ");
            Integer rateTypeInt = scanner.nextInt();
            
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


        Long newRoomRateId = roomRateSessionBeanRemote.createNewRoomRate(newRoomRate);
        System.out.println("New room type created successfully!: " + newRoomRateId + "\n");
    }

    private void doViewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Rate Details ***\n");
        System.out.print("Enter Room Rate ID> ");
        Long roomRateId = scanner.nextLong();

        try {
            RoomRate roomRate = roomRateSessionBeanRemote.retrieveRoomRateById(roomRateId);
            if (roomRate.getRateType() == RateTypeEnum.PEAK || roomRate.getRateType() == RateTypeEnum.PROMOTION) {
                System.out.printf("%-12s %-12s %-12s %-12s %-12s %-12s %-12s \n", "Room Rate Id", "Name",
                        "Rate Type", "Rate Per Night", "Valid From", "Valid To", "Room Type Id");
                System.out.printf("%-12s %-12s %-12s %-12s %-12s  %-12s %-12s \n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), roomRate.getValidFrom(), roomRate.getValidTo(), roomRate.getRoomType());
            }
            else
            {
                System.out.printf("%-12s %-12s %-12s %-12s %-12s \n", "Room Rate Id", "Name",
                    "Rate Type", "Rate Per Night", "Room Type Id");
                System.out.printf("%-12s %-12s %-12s %-12s %-12s \n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                    roomRate.getRateType(), roomRate.getRatePerNight(), roomRate.getRoomType());
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
        
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View All Room Rates ***\n");
        
        List<RoomRate> roomRates = roomRateSessionBeanRemote.viewAllRoomRates();
        System.out.printf("%-12s %-12s %-12s %-12s %-12s %-12s %-12s \n", "Room Rate Id", "Name",
                        "Rate Type", "Rate Per Night", "Valid From", "Valid To", "Room Type Id");

        for(RoomRate roomRate:roomRates)
        {
            if (roomRate.getRateType() == RateTypeEnum.PEAK || roomRate.getRateType() == RateTypeEnum.PROMOTION) {
                System.out.printf("%-12s %-12s %-12s %-12s %-12s  %-12s %-12s \n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), roomRate.getValidFrom(), roomRate.getValidTo(), roomRate.getRoomType());
            }
            else 
            {
                System.out.printf("%-12s %-12s %-12s %-12s %-12s  %-12s %-12s \n", roomRate.getRoomRateId(), roomRate.getRoomRateName(),
                        roomRate.getRateType(), roomRate.getRatePerNight(), "N/A", "N/A", roomRate.getRoomType());
            }
        }
        
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateRoomRate(RoomRate roomRate) {
        Scanner scanner = new Scanner(System.in);        
        String input;
        // name, rate type, rate per night, validfrom, validto, roomtypeid
        System.out.println("*** Hotel Reservation System :: Hotel Operation :: View Room Rate Details :: Update Room Rate ***\n");
        System.out.print("Enter Name (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            roomRate.setRoomRateName(input);
        }
                
        System.out.print("Enter Rate Per Night(blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0)
        {
            BigDecimal ratePerNight = new BigDecimal(input);
            roomRate.setRatePerNight(ratePerNight);
        }
        
        System.out.print("Enter Room Type Id (blank if no change)> ");
        input = scanner.nextLine().trim();
        if(input.length() > 0) {
            Long roomTypeId = Long.parseLong(scanner.nextLine().trim());
            RoomType roomType;
            try {
                roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
                roomRate.setRoomType(roomType);
            } catch (RoomTypeNotFoundException ex) {
                System.out.println("An error has occurred while retrieving room type: " + ex.getMessage() + "\n");
            }
        }
        
        while(true)
        {
            System.out.print("Enter Room Rate Type (1: Published, 2: Normal, 3: Peak, 4: Promotion, blank if no change)> ");
            Integer rateTypeInt = scanner.nextInt();
            
            if(input.length() > 0) 
            {
                roomRate.setRateType(RateTypeEnum.values()[rateTypeInt-1]);
                break;
            }
            else
            {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (roomRate.getRateType() == RateTypeEnum.PEAK || roomRate.getRateType() == RateTypeEnum.PROMOTION) {
            try {
               System.out.print("Enter Room Rate Start Date (yyyy-MM-dd) (blank if no change)> ");
               String startDateInput = scanner.nextLine().trim();
               if(startDateInput.length() > 0) {
                    Date startDate = dateFormat.parse(startDateInput); // Convert String to Date
                    roomRate.setValidFrom(startDate);
               }
               System.out.print("Enter Room Rate End Date (yyyy-MM-dd) (blank if no change)> ");
               String endDateInput = scanner.nextLine().trim();
               if(endDateInput.length() > 0) {
                    Date endDate = dateFormat.parse(endDateInput); // Convert String to Date
                    roomRate.setValidTo(endDate);
               }
            } catch (ParseException ex) {
               System.out.println("Please enter a valid Room Rate! \n");
            }
        }
        roomRateSessionBeanRemote.updateRoomRate(roomRate);
        System.out.println("Room type updated successfully!\n");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRooms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRoomAllocationExceptionReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}