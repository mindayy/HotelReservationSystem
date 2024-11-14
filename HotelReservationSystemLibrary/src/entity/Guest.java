/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;


/**
 *
 * @author min
 */

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Guest implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    @Column(nullable = false, length = 64, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "guest", cascade = {}, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Guest() {
    }

    public Guest(String email) {
        this.email = email;

    }
    
    
    /**
     * @return the guestId
     */
    public Long getGuestId() {
        return guestId;
    }

    /**
     * @param guestId the guestId to set
     */
    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
        public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getGuestId() != null ? getGuestId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof Guest)) {
            return false;
        }
        Guest other = (Guest) object;
        if ((this.getGuestId() == null && other.getGuestId() != null) || (this.getGuestId() != null && !this.guestId.equals(other.guestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + getGuestId() + " ]";
    }


}

