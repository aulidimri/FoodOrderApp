package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {
    @PersistenceContext
    private EntityManager entityManager;
    public Coupon findByName(final String couponName) {
        try {
            return entityManager.createNamedQuery("couponByCouponName", Coupon.class).setParameter("couponName", couponName).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Coupon findById(final long id) {
        try {
            return entityManager.createNamedQuery("couponById", Coupon.class).setParameter("id", id).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public Coupon findByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("couponByUuid", Coupon.class).setParameter("uuid", uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
}
