package entity;

import enums.RoleEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author kaixin
 */
@Entity
public class Employee implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 64)
    private String password; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role;
    
    @Column(nullable = false, length = 60)
    private String email;
    
    @Column(nullable = false)
    private boolean loggedIn = false;


    public Employee(){
        
    }
    
    public Employee(String username, String password, RoleEnum role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.loggedIn = false;
        
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    
    public String getUsername() {
        return username;
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public RoleEnum getRole() {
        return role;
    }
    
    public void setRole(RoleEnum role) {
        this.role = role;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.employee[ id=" + employeeId + " ]";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
      
    
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
}