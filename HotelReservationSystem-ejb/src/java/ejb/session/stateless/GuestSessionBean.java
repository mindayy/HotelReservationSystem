/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;

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
            throw new GuestNotFoundException("Employee ID " + guestId + " does not exist.");
        }
    }



}
