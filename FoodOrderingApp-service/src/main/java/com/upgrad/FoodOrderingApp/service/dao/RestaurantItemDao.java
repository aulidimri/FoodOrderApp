package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantItem> getItemsByRestaurant(int restaurant_id) {
        try {
            return entityManager.createNamedQuery("getItemsByRestaurant", RestaurantItem.class).setParameter("restaurant_id", restaurant_id).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
