/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author kaixin
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public PartnerSessionBean() {
    }

    @Override
    public void createPartner(String username, String password, String email) {
        // Check if username or email already exists
        Query query = em.createQuery("SELECT COUNT(p) FROM Partner p WHERE p.username = :username OR p.email = :email");
        query.setParameter("username", username);
        query.setParameter("email", email);
        long count = (long)query.getSingleResult();

        if (count > 0) {
            throw new RuntimeException("Username or email already exists.");
        }

        // Create new partner and persist
        Partner partner = new Partner(username, password, email);
        em.persist(partner); 
    }

    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");
        return query.getResultList();
    }
    
    
}
