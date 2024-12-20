/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Guest;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author min
 */
@Remote
public interface GuestSessionBeanRemote {
    
    public Long registerGuest(String email, String username, String password);
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;
    
    public Customer guestLogin(String username, String password) throws InvalidLoginCredentialException;
    
    public Guest createNewGuest(Guest newGuest);
}
