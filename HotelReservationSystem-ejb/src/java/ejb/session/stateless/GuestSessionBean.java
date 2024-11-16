/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Guest;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author min
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public GuestSessionBean() {
    }

    @Override
    public Long registerGuest(String email, String username, String password) {
        Customer guest = new Customer(email, username, password, false);
        em.persist(guest);
        em.flush();
        return guest.getGuestId();
    }
    
    @Override
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        if (guest != null) {
            return guest;
        } else {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist.");
        }
    }
    
    @Override
    public Customer guestLogin(String username, String password) throws InvalidLoginCredentialException {
        // Query should target the Customer class as it has username and password
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            // Retrieve the Customer (not Guest)
            Customer customer = (Customer) query.getSingleResult();

            // Check if the guest is already logged in
            if (customer.isIsLoggedIn()) {
                System.out.println("Customer is already logged in.");
                return customer; // Optionally return the logged-in customer
            }

            // Check if the password matches
            if (customer.getPassword().equals(password)) {
                customer.setIsLoggedIn(true);  // Set login status
                em.merge(customer);  // Persist the change
                return customer;
            } else {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }

    
    @Override
    public Reservation viewReservationDetails(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " not found.");
        }
        return reservation;
    }


    @Override
    public List<Reservation> viewAllMyReservations(Long guestId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :guestId");
        query.setParameter("guestId", guestId);
        return query.getResultList();
    }


    @Override
    public Guest createNewGuest(Guest newGuest) {
        // Persist the new guest entity
        em.persist(newGuest);
        em.flush(); // Ensure the entity is synchronized with the database

        return newGuest; // Return the persisted guest with the generated ID
    } 


}
