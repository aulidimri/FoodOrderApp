package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {
    @PersistenceContext
    private EntityManager entityManager;


    public Category findByCategoryId(String uuid) {
        try {
            return entityManager.createNamedQuery("getByCategoryId", Category.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public List<RestaurantCategory> findRestaurantsByCategoryId(Category category) {
        try {
            return entityManager.createNamedQuery("getRestaurantsByCategoryId", RestaurantCategory.class).setParameter("category",category).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public List<Category> getAllCategories() {
        try {
            return entityManager.createNamedQuery("getAllCategories", Category.class).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
