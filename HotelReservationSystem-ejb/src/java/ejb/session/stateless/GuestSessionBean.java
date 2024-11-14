/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
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
    public void registerGuest(String email, String username, String password) {
        Customer guest = new Customer(email, username, password, false);
        em.persist(guest);
    }
    
    @Override
    public Customer retrieveGuestById(Long guestId) throws GuestNotFoundException {
        Customer guest = em.find(Customer.class, guestId);
        if (guest != null) {
            return guest;
        } else {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist.");
        }
    }
    
    @Override
    public Customer guestLogin(String username, String password) throws InvalidLoginCredentialException {
        Query query = em.createQuery("SELECT e FROM Guest g WHERE g.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            Customer guest = (Customer) query.getSingleResult();
            if (guest.isIsLoggedIn()) {
                System.out.println("Guest is already logged in.");
            }
            if (guest.getPassword().equals(password)) {
                guest.setIsLoggedIn(true); 
                em.merge(guest);
                return guest;
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

    /*
    @Override
    public List<Reservation> viewAllMyReservations(Long guestId) {
        Customer guest = em.find(Customer.class, guestId);
        List<Reservation> reservations = guest.getReservations();

        return reservations;
    }
    */
    
    @Override
    public List<Reservation> viewAllMyReservations(Long guestId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.guest.guestId = :guestId");
        query.setParameter("guestId", guestId);
        return query.getResultList();
    }




}
