/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.ReservationRoom;
import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.ReservationNotFoundException;
import util.exception.RoomNotAvailableException;

/**
 *
 * @author kaixin
 */
@Local
public interface ReservationRoomSessionBeanLocal {
    
    public List<Room> searchAvailableRooms(Date checkInDate, Date checkOutDate, Long roomTypeId);
    
    public ReservationRoom reserveRoom(Long roomId, Long reservationId, Date checkInDate, Date checkOutDate) throws RoomNotAvailableException, ReservationNotFoundException;
    
    
}
