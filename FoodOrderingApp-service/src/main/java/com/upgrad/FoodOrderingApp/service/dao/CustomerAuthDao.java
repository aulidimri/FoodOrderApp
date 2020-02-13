package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAuth findByAccessToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("customerByAccessToken", CustomerAuth.class).setParameter("accessToken", accessToken).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuth updateLogout(final CustomerAuth customerAuth) {
        try{
            entityManager.persist(customerAuth);
            return customerAuth;
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuth findByCustomer(Customer customer) {
        try {
            return entityManager.createNamedQuery("customerAuthByCustomer", CustomerAuth.class).setParameter("customer", customer).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
