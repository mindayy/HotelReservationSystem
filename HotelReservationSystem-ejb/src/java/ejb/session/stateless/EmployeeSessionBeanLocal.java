/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import enums.RoleEnum;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaixin
 */
@Local
public interface EmployeeSessionBeanLocal {
    public void createEmployee(String username, String password, RoleEnum role, String email);
    
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
    public List<Employee> retrieveAllEmployees();
    
    public void employeeLogout(Long employeeId) throws EmployeeNotFoundException;
   
}