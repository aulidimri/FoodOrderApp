package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.Payment;
import com.upgrad.FoodOrderingApp.service.entity.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StateDao {
    @PersistenceContext
    private EntityManager entityManager;

    public State findByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", State.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public List<State> getAllStates() {
        try {
            return entityManager.createNamedQuery("getStates", State.class).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public State findById(long id) {
        try {
            return entityManager.createNamedQuery("stateById", State.class).setParameter("id", id).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
