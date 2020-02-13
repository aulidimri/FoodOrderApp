package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategory> findCategoriesByRestaurant(Restaurant restaurant) {
        try {
            return entityManager.createNamedQuery("getCategoriesById", RestaurantCategory.class).setParameter("restaurant", restaurant).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
