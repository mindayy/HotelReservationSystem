package ejb.session.stateless;

import entity.Employee;
import enums.RoleEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public EmployeeSessionBean() {
    }

    @Override
    public void createEmployee(String username, String password, RoleEnum role, String email) {
        Employee employee = new Employee(username, password, role, email);
        em.persist(employee);
    }

    @Override
    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist.");
        }
    }


    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            Employee employee = (Employee) query.getSingleResult();
            if (employee.isLoggedIn()) {
                //throw new InvalidLoginCredentialException("Employee is already logged in.");
                System.out.println("Employee is already logged in.");
            }
            if (employee.getPassword().equals(password)) {
                employee.setLoggedIn(true); 
                em.merge(employee);
                return employee;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");
        return query.getResultList();
    }

    @Override
    public void employeeLogout(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);
        if (employee != null) {
            if (employee.isLoggedIn()) {
                employee.setLoggedIn(false);  
                em.merge(employee);
            } else {
                throw new IllegalStateException("Employee is not logged in.");
            }
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist.");
        }
    }
    
    
}
