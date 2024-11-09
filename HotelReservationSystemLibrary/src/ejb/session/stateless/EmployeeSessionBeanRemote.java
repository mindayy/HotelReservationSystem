/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import enums.RoleEnum;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author kaixin
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public void createEmployee(String username, String password, RoleEnum role, String email);
    
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
    
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;
    
    public List<Employee> retrieveAllEmployees();
    
    public void employeeLogout(Long employeeId) throws EmployeeNotFoundException;
    
}
