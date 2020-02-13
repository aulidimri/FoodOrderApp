package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Restaurant> getRestaurants() {
        try {
            return entityManager.createNamedQuery("getRestaurants", Restaurant.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        entityManager.persist(restaurant);
        return restaurant;
    }

    public List<Restaurant> getRestaurantsByName(String restaurantName) {
        try {
            return entityManager.createNamedQuery("getRestaurantsByName", Restaurant.class).setParameter("restaurantName", "%"+restaurantName+"%").getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Restaurant getRestaurantById(int id) {
        try {
            return entityManager.createNamedQuery("getRestaurantById", Restaurant.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Restaurant getRestaurantByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("getRestaurantByUuid", Restaurant.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<Restaurant> getRestaurantByCategory(Restaurant restaurant) {
        try {
            return entityManager.createNamedQuery("getRestaurantByCategory", Restaurant.class).setParameter("restaurant", restaurant).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
