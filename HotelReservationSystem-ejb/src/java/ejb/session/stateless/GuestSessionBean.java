/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

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
        Guest guest = new Guest(email, username, password, false);
        em.persist(guest);
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
    public Guest guestLogin(String username, String password) throws InvalidLoginCredentialException {
        Query query = em.createQuery("SELECT e FROM Guest g WHERE g.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            Guest guest = (Guest) query.getSingleResult();
            if (guest.isLoggedIn()) {
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
    
    



}
