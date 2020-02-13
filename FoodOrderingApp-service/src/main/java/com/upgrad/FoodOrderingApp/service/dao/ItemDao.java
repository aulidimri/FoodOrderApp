package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Item getItemById(long id) {
        try {
            return entityManager.createNamedQuery("findItemById", Item.class).setParameter("id",id).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
    public Item getItemByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("findItemByUuid", Item.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
