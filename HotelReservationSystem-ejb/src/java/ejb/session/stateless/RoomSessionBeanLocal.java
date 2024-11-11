/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import enums.RoomStatusEnum;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomNotFoundException;

/**
 *
 * @author kaixin
 */
@Local
public interface RoomSessionBeanLocal {
    
    public void createRoom(String roomNumber, RoomType roomType, RoomStatusEnum roomStatus);
    
    public void updateRoom(Room updatedRoom) throws RoomNotFoundException;
    
    public void deleteRoom(Long roomId) throws RoomNotFoundException;
    
    public List<Room> retrieveAllRooms();
    
}
