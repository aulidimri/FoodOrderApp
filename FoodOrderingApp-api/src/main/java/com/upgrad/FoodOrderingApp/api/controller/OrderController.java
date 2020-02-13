package com.upgrad.FoodOrderingApp.api.controller;

import com.sun.tools.javac.jvm.Items;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}")
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(String accessToken, @PathVariable String coupon_name) throws CouponNotFoundException, AuthorizationFailedException {
        Coupon coupon = orderService.getCouponByName(accessToken, coupon_name);
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse().couponName(coupon.getCouponName()).percent(coupon.getPercent()).id(UUID.fromString(coupon.getUuid()));
        return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/order")
    public ResponseEntity<CustomerOrderResponse> getPastOrdersOfUser(String accessToken) throws AuthorizationFailedException {
        List<Order> orderList = orderService.getPastOrders(accessToken);
        List<OrderList> orderLists = new ArrayList<>();
        for(Order order : orderList){
            Coupon coupon = orderService.getCouponById(order.getCoupon().getId());
            OrderListCoupon orderListCoupon = new OrderListCoupon().couponName(coupon.getCouponName()).percent(coupon.getPercent())
                    .id(UUID.fromString(coupon.getUuid()));
            Payment payment = paymentService.getPaymentById(order.getPayment().getId());
            OrderListPayment orderListPayment = new OrderListPayment().paymentName(payment.getPaymentName()).id(UUID.fromString(payment.getUuid()));
            Customer customer = customerService.findByCustomerId(order.getCustomer().getId());
            OrderListCustomer orderListCustomer = new OrderListCustomer().id(UUID.fromString(customer.getUuid())).firstName(customer.getFirstName())
                    .lastName(customer.getLastName()).emailAddress(customer.getEmail()).contactNumber(customer.getContactNumber());
            Address address = addressService.getAddressById(order.getAddress().getId());
            State state = addressService.getStateById(address.getState().getId());
            OrderListAddressState orderListAddressState = new OrderListAddressState().stateName(state.getStateName()).id(UUID.fromString(state.getUuid()));
            OrderListAddress orderListAddress = new OrderListAddress().id(UUID.fromString(address.getUuid())).flatBuildingName(address.getFlatBuildingNumber())
                   .locality(address.getLocality()).city(address.getCity()).pincode(address.getPincode()).state(orderListAddressState);
           // OrderItem orderItem = orderService.getOrderItemsBy
            List<ItemQuantityResponse> itemQuantityResponses = new ArrayList<>();
            OrderList orderList1 = new OrderList().id(UUID.fromString(order.getUuid())).bill(order.getBill()).coupon(orderListCoupon)
                    .discount(order.getDiscount()).date(order.getDate()).payment(orderListPayment).customer(orderListCustomer).address(orderListAddress).itemQuantities(itemQuantityResponses);
        }
        CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse().orders(orderLists);
        return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/order")
    public ResponseEntity<SaveOrderResponse> saveOrder(String accessToken, SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, PaymentMethodNotFoundException, CouponNotFoundException, RestaurantNotFoundException, AddressNotFoundException {
        Order order = new Order();
        Address address = new Address();
        address.setUuid(saveOrderRequest.getAddressId());
        order.setAddress(address);
        order.setBill(saveOrderRequest.getBill());
       order.setDiscount(saveOrderRequest.getDiscount());
       Coupon coupon = new Coupon();
       coupon.setUuid(saveOrderRequest.getCouponId().toString());
       order.setCoupon(coupon);
       Payment payment = new Payment();
       payment.setUuid(saveOrderRequest.getPaymentId().toString());
       order.setPayment(payment);
       Restaurant restaurant = new Restaurant();
       restaurant.setUuid(saveOrderRequest.getRestaurantId().toString());
       order.setRestaurant(restaurant);
       List<ItemQuantity> items = saveOrderRequest.getItemQuantities();
       Order savedOrder = orderService.saveOrder(accessToken, order);
        SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(savedOrder.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.OK);
    }
}
