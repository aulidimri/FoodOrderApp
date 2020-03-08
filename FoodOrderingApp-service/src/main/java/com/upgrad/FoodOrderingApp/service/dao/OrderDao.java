package com.upgrad.FoodOrderingApp.service.dao;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.Order;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> getAllOrders(final Customer customer) {
        try {
            return entityManager.createNamedQuery("getOrdersByCustId", Order.class).setParameter("customer", customer).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Order saveOrder(Order order) {
        try{
            entityManager.persist(order);
            return order;
        }
        catch (NoResultException nre){
            return null;
        }
        }
}
