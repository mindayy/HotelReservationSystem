/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Employee;
import enums.RoleEnum;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author kaixin
 */
@Singleton
@LocalBean

public class DataInitialisationSessionBean {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public DataInitialisationSessionBean() {
    }

    @PostConstruct
    public void initializeData () {
        // Check if system administrator exists, if not, create a default admin
        try {
            em.createQuery("SELECT e FROM Employee e WHERE e.username = :username")
              .setParameter("username", "admin")
              .getSingleResult();
        } catch (NoResultException ex) {
            // If admin doesn't exist, create one
            Employee admin = new Employee("sysadmin1", "password", RoleEnum.SystemAdministrator, "admin@hotel.com");
            em.persist(admin);
            admin = new Employee("opmanager1", "password", RoleEnum.OperationManager, "admin@hotel.com");
            em.persist(admin);
            admin = new Employee("salesmanager1", "password", RoleEnum.SalesManager, "admin@hotel.com");
            em.persist(admin);
            admin = new Employee("guestrelo1", "password", RoleEnum.GuestRelationOfficer, "admin@hotel.com");
            em.persist(admin);
        }
    }
}
