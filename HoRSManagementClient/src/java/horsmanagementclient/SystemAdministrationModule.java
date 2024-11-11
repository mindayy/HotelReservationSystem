/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsmanagementclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import entity.Employee;
import entity.Partner;
import enums.RoleEnum;
import java.util.List;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author min
 */
public class SystemAdministrationModule {
    
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    
    private Employee currentEmployee;
    
    public SystemAdministrationModule(){
        
    }

    public SystemAdministrationModule(EmployeeSessionBeanRemote employeeSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, Employee currentEmployee) {
        this();
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuSystemAdministration() throws InvalidAccessRightException {

        if (currentEmployee.getRole() != RoleEnum.SystemAdministrator) {
            throw new InvalidAccessRightException("You don't have SYSTEM ADMINISTRATOR rights.");
        }
        
        Scanner sc = new Scanner(System.in);
        int response = 0;

        while (true) {
            System.out.println("*** HoRS Management System :: System Administration ***\n");
            System.out.println("1: Create New Employee");
            System.out.println("2: View All Employees");
            System.out.println("3: Create New Partner");
            System.out.println("4: View All Partners");
            System.out.println("5: Exit\n");
            System.out.print("> ");

            response = sc.nextInt();

            if (response == 1) {
                doCreateNewEmployee();
            } else if (response == 2) {
                doViewAllEmployees();
            } else if (response == 3) {
                doCreateNewPartner();
            } else if (response == 4) {
                doViewAllPartners();
            } else if (response == 5) {
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
    }
    
    private void doCreateNewEmployee() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** HoRS Management System :: Create New Employee ***\n");

        System.out.print("Enter Employee Username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = sc.nextLine().trim();
        System.out.print("Enter Email> ");
        String email = sc.nextLine().trim();

        // Display available roles
        System.out.println("Select Role:");
        for (RoleEnum role : RoleEnum.values()) {
            System.out.println((role.ordinal() + 1) + ": " + role);
        }
        int roleChoice = sc.nextInt();
        RoleEnum selectedRole = RoleEnum.values()[roleChoice - 1];

        try {
            employeeSessionBeanRemote.createEmployee(username, password, selectedRole, email);
            System.out.println("New employee created successfully!\n");
        } catch (Exception ex) {
            System.out.println("An error occurred while creating the employee: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllEmployees() {
        System.out.println("*** HoRS Management System :: View All Employees ***\n");
        List<Employee> employees = employeeSessionBeanRemote.retrieveAllEmployees();

        for (Employee employee : employees) {
            System.out.println("Employee ID: " + employee.getEmployeeId() + ", Username: " + employee.getUsername() + ", Role: " + employee.getRole() + ", Email: " + employee.getEmail());
        }
        System.out.println();
    }

    private void doCreateNewPartner() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** HoRS Management System :: Create New Partner ***\n");

        System.out.print("Enter Partner Username> ");
        String username = sc.nextLine().trim();
        System.out.print("Enter Email> ");
        String email = sc.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = sc.nextLine().trim();

        try {
            partnerSessionBeanRemote.createPartner(username, password, email);
            System.out.println("New partner created successfully!\n");
        } catch (Exception ex) {
            ex.printStackTrace();  // Print full stack trace for better debugging
            System.out.println("An error occurred while creating the partner: " + ex.getMessage() + "\n");
        }
    }

    private void doViewAllPartners() {
        System.out.println("*** HoRS Management System :: View All Partners ***\n");
        List<Partner> partners = partnerSessionBeanRemote.retrieveAllPartners();

        for (Partner partner : partners) {
            System.out.println("Partner ID: " + partner.getPartnerId() + ", Username: " + partner.getUsername() + ", Email: " + partner.getEmail());
        }
        System.out.println();
    }
    
}
