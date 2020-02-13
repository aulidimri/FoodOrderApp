package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryItem> findCategoryItemById(Category category) {
        try {
            return entityManager.createNamedQuery("categoryItemById", CategoryItem.class).setParameter("category", category).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
