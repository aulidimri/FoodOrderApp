package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Address;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Address saveAddress(Address address) {
        entityManager.persist(address);
        return address;
    }

    public Address deleteAddress(Address address){
        entityManager.remove(address);
        return address;
    }

    public List<Address> getAllSavedAddress() {
        try {
            return entityManager.createNamedQuery("getSavedAddress", Address.class).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Address getAddressById(long id){
        try {
            return entityManager.createNamedQuery("getAddressById", Address.class).setParameter("id", id).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Address getAddressByUuid(String uuid){
        try {
            return entityManager.createNamedQuery("getAddressByUuid", Address.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
