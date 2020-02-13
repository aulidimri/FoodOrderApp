package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Customer createCustomer(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    public Customer findByContactNo(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNo", Customer.class).setParameter("contactNumber", contactNumber).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Customer findById(final int customerId) {
        try {
            return entityManager.createNamedQuery("customerById", Customer.class).setParameter("id", customerId).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuth createAuthToken(final CustomerAuth customerAuth) {
        entityManager.persist(customerAuth);
        return customerAuth;
    }

    public Customer updateCustomerDetails(final Customer customer) {
        try{
            entityManager.persist(customer);
            return customer;
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
