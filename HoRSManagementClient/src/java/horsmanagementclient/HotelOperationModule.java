/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import entity.Employee;
import entity.RoomType;
import enums.RoleEnum;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
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
    
    

    void menuHotelOperation() throws InvalidAccessRightException{
        if (currentEmployee.getRole() != RoleEnum.OperationManager || currentEmployee.getRole() != RoleEnum.SalesManager) {
            throw new InvalidAccessRightException("You do not have the rights to access the System Administration Module.");
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
        newRoomType.setIsDisabled(Boolean.FALSE);

        Long newRoomTypeId = roomTypeSessionBeanRemote.createNewRoomType(newRoomType);
        System.out.println("New room type created successfully!: " + newRoomTypeId + "\n");

    }

    private void doViewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        System.out.println("*** Hotel Reservation System :: System Administration :: View Room Type Details ***\n");
        System.out.print("Enter Room Type ID> ");
        Long roomTypeId = scanner.nextLong();

        try {
            RoomType roomType = roomTypeSessionBeanRemote.retrieveRoomTypeById(roomTypeId);
            System.out.printf("%8s%20s%64s%8s%20s%8s%64s\n", "Room Type Id", "Name",
                    "Description", "Size", "Bed", "Capacity", "Amenities");
            System.out.printf("%8s%20s%64s%8s%20s%8s%64s\n", roomType.getRoomTypeId(), roomType.getName(),
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
        System.out.printf("Confirm Delete Room Tuype %s (Room Type ID: %d) (Enter 'Y' to Delete)> ", roomType.getName(), roomType.getRoomTypeId());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private void doCreateNewRoomRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRoomRateDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRoomRates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
