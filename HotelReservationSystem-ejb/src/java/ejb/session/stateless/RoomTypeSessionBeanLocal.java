/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import javax.ejb.Local;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author min
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createNewRoomType(RoomType roomType);

    public void updateRoomType(Long roomTypeId, String newName, String newDescription, 
            int newSize, String newBed, int newCapacity, String newAmenities, 
            Boolean isDisabled, Boolean roomTypeStatus) throws RoomTypeNotFoundException;
    
}
