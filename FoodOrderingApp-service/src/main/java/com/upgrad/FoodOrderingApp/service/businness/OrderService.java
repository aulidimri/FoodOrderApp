package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private ItemDao itemDao;

    public Coupon getCouponByName(String accessToken, String couponName) throws CouponNotFoundException, AuthorizationFailedException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        Coupon coupon = couponDao.findByName(couponName);
        if(coupon == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        if(couponName.isEmpty()){
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        return coupon;
    }

    public List<Order> getPastOrders(String accessToken) throws AuthorizationFailedException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        List<Order> orderList = new ArrayList<>();
        Customer customer = customerDao.findById(customerAuth.getCustomer().getId());
        List<Order> orders = orderDao.getAllOrders(customer);
        return orders;
    }

    public Coupon getCouponById(long id) {
        return couponDao.findById(id);
    }

    public Coupon getCouponByUuid(String uuid) {
        return couponDao.findByUuid(uuid);
    }

    @Transactional
    public Order saveOrder(String accessToken, Order order) throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        Coupon coupon = couponDao.findByUuid(order.getCoupon().getUuid());
        if(coupon == null){
            throw new CouponNotFoundException("CPF-002", "No coupon by this id");
        }
        Address address = addressDao.getAddressByUuid(order.getAddress().getUuid());
        if(address == null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        Payment payment = paymentDao.getPaymentByUuid(order.getPayment().getUuid());
        if(payment == null){
            throw new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id");
        }
        Restaurant restaurant = restaurantDao.getRestaurantByUuid(order.getRestaurant().getUuid());
        if(restaurant == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }

        return orderDao.saveOrder(order);

    }


}
