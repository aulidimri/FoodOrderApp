package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Order;
import com.upgrad.FoodOrderingApp.service.entity.Payment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PaymentDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Payment> getAllPaymentMethods() {
        try {
            return entityManager.createNamedQuery("getPaymentMethods", Payment.class).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Payment getPaymentById(int id) {
        try {
            return entityManager.createNamedQuery("getPaymentById", Payment.class).setParameter("id", id).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Payment getPaymentByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("getPaymentByUuid", Payment.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
