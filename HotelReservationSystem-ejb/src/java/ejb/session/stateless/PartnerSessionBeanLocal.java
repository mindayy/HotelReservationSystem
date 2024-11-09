/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author kaixin
 */
@Local
public interface PartnerSessionBeanLocal {
    
    public void createPartner(String username, String password, String email);
    
    public List<Partner> retrieveAllPartners();
    
}
