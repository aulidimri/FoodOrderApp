package com.upgrad.FoodOrderingApp.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address")
@NamedQueries({@NamedQuery(name = "getSavedAddress", query = "select a from Address a order by a.id"),
        @NamedQuery(name = "getAddressById", query = "select a from Address a where a.id = :id"),
        @NamedQuery(name = "getAddressByUuid", query = "select a from Address a where a.uuid = :uuid")
})
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "flat_buil_number")
    private String flatBuildingNumber;

    @Column(name= "locality")
    private String locality;

    @Column(name= "city")
    private String city;

    @Column(name= "pincode")
    private String pincode;

    @Column(name= "active", columnDefinition = "int default 1")
    private int active;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private State state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFlatBuildingNumber() {
        return flatBuildingNumber;
    }

    public void setFlatBuildingNumber(String flatBuildingNumber) {
        this.flatBuildingNumber = flatBuildingNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
