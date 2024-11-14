/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import entity.Employee;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import enums.RateTypeEnum;
import enums.RoleEnum;
import enums.RoomStatusEnum;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author min
 */
@Singleton
@LocalBean
// @Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void initializeData() {
        // Initialize Employees
        createEmployee("sysadmin", "password", RoleEnum.SystemAdministrator, "sysadmin@mail.com");
        createEmployee("opmanager", "password", RoleEnum.OperationManager, "opmanager@mail.com");
        createEmployee("salesmanager", "password", RoleEnum.SalesManager, "salesmanager@mail.com");
        createEmployee("guestrelo", "password", RoleEnum.GuestRelationOfficer, "guestrelo@mail.com");

        // Initialize Room Types
        createRoomType("Grand Suite", null, "Luxurious and spacious", 70, "Two Kings", 4, "TV, free minibar"); 
        createRoomType("Junior Suite", "Grand Suite", "Stylish with a living area", 50, "Two Kings", 4, "TV, free minibar");
        createRoomType("Family Room", "Junior Suite", "Ideal for families", 45, "King + Queen", 4, "TV, minibar");
        createRoomType("Premier Room", "Family Room", "Spacious ", 35, "King", 2, "TV, free minibar");
        createRoomType("Deluxe Room", "Premier Room", "Comfortable and modern", 25, "Queen", 2, "TV, minibar");

        // Initialize Room Rates
        createRoomRate("Deluxe Room Published", "Deluxe Room", RateTypeEnum.PUBLISHED, new BigDecimal(100));
        createRoomRate("Deluxe Room Normal", "Deluxe Room", RateTypeEnum.NORMAL, new BigDecimal(50));
        createRoomRate("Premier Room Published", "Premier Room", RateTypeEnum.PUBLISHED, new BigDecimal(200));
        createRoomRate("Premier Room Normal", "Premier Room", RateTypeEnum.NORMAL, new BigDecimal(100));
        createRoomRate("Family Room Published", "Family Room", RateTypeEnum.PUBLISHED, new BigDecimal(300));
        createRoomRate("Family Room Normal", "Family Room", RateTypeEnum.NORMAL, new BigDecimal(150));
        createRoomRate("Junior Suite Published", "Junior Suite", RateTypeEnum.PUBLISHED, new BigDecimal(400));
        createRoomRate("Junior Suite Normal", "Junior Suite", RateTypeEnum.NORMAL, new BigDecimal(200));
        createRoomRate("Grand Suite Published", "Grand Suite", RateTypeEnum.PUBLISHED, new BigDecimal(500));
        createRoomRate("Grand Suite Normal", "Grand Suite", RateTypeEnum.NORMAL, new BigDecimal(250));

        // Initialize Rooms
        createRoom("Deluxe Room", "0101", RoomStatusEnum.AVAILABLE);
        createRoom("Deluxe Room", "0201", RoomStatusEnum.AVAILABLE);
        createRoom("Deluxe Room", "0301", RoomStatusEnum.AVAILABLE);
        createRoom("Deluxe Room", "0401", RoomStatusEnum.AVAILABLE);
        createRoom("Deluxe Room", "0501", RoomStatusEnum.AVAILABLE);
        createRoom("Premier Room", "0102", RoomStatusEnum.AVAILABLE);
        createRoom("Premier Room", "0202", RoomStatusEnum.AVAILABLE);
        createRoom("Premier Room", "0302", RoomStatusEnum.AVAILABLE);
        createRoom("Premier Room", "0402", RoomStatusEnum.AVAILABLE);
        createRoom("Premier Room", "0502", RoomStatusEnum.AVAILABLE);
        createRoom("Family Room", "0103", RoomStatusEnum.AVAILABLE);
        createRoom("Family Room", "0203", RoomStatusEnum.AVAILABLE);
        createRoom("Family Room", "0303", RoomStatusEnum.AVAILABLE);
        createRoom("Family Room", "0403", RoomStatusEnum.AVAILABLE);
        createRoom("Family Room", "0503", RoomStatusEnum.AVAILABLE);
        createRoom("Junior Suite", "0104", RoomStatusEnum.AVAILABLE);
        createRoom("Junior Suite", "0204", RoomStatusEnum.AVAILABLE);
        createRoom("Junior Suite", "0304", RoomStatusEnum.AVAILABLE);
        createRoom("Junior Suite", "0404", RoomStatusEnum.AVAILABLE);
        createRoom("Junior Suite", "0504", RoomStatusEnum.AVAILABLE);
        createRoom("Grand Suite", "0105", RoomStatusEnum.AVAILABLE);
        createRoom("Grand Suite", "0205", RoomStatusEnum.AVAILABLE);
        createRoom("Grand Suite", "0305", RoomStatusEnum.AVAILABLE);
        createRoom("Grand Suite", "0405", RoomStatusEnum.AVAILABLE);
        createRoom("Grand Suite", "0505", RoomStatusEnum.AVAILABLE);
    }

    private void createEmployee(String username, String password, RoleEnum userRole, String email) {
        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setRole(userRole);
        employee.setEmail(email);
        employee.setLoggedIn(false);
        em.persist(employee);
    }

    private RoomType createRoomType(String name, String nextHigherRoomTypeName, String description, int size, String bed, int capacity, String amenities) {
        RoomType roomType = new RoomType();
        roomType.setName(name);
        RoomType nextHigherRoomType = null;
        if (nextHigherRoomTypeName != null) {
            nextHigherRoomType = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name", RoomType.class)
                                   .setParameter("name", nextHigherRoomTypeName)
                                   .getSingleResult();
        }

        roomType.setNextHigherRoomType(nextHigherRoomType);
        roomType.setDescription(description);
        roomType.setSize(size);
        roomType.setBed(bed);
        roomType.setCapacity(capacity);
        roomType.setAmenities(amenities);
        roomType.setIsDisabled(false);
        em.persist(roomType);
        return roomType;
    }

    private void createRoomRate(String name, String roomTypeName, RateTypeEnum rateType, BigDecimal ratePerNight) {
        RoomType roomType = null;
        if (roomTypeName != null) {
            roomType = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name", RoomType.class)
                                   .setParameter("name", roomTypeName)
                                   .getSingleResult();
        }
        RoomRate roomRate = new RoomRate();
        roomRate.setRoomRateName(name);
        roomRate.setRoomType(roomType);
        roomRate.setRateType(rateType);
        roomRate.setRatePerNight(ratePerNight);
        roomRate.setIsDisabled(Boolean.FALSE);
        em.persist(roomRate);
    }

    private void createRoom(String roomTypeName, String roomNumber, RoomStatusEnum roomStatus) {
        RoomType roomType = em.createQuery("SELECT r FROM RoomType r WHERE r.name = :name", RoomType.class)
                               .setParameter("name", roomTypeName)
                               .getSingleResult();
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setRoomStatus(roomStatus);
        em.persist(room);
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
