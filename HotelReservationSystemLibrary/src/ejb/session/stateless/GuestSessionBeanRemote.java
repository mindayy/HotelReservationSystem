/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;

/**
 *
 * @author min
 */
@Remote
public interface GuestSessionBeanRemote {
    
    public void registerGuest(String email, String username, String password);
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;
    
}
