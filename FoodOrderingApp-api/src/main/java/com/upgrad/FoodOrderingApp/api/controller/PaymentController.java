package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import com.upgrad.FoodOrderingApp.service.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET, path = "/payment")
    public ResponseEntity<PaymentListResponse> getPaymentMethods() {
        List<Payment> paymentMethods = paymentService.getPaymentMethods();
        List<PaymentResponse> paymentResponses = new ArrayList<>();
        for (Payment payment : paymentMethods) {
            PaymentResponse paymentResponse = new PaymentResponse().paymentName(payment.getPaymentName()).id(UUID.fromString(payment.getUuid()));
            paymentResponses.add(paymentResponse);
        }
        PaymentListResponse paymentListResponse = new PaymentListResponse().paymentMethods(paymentResponses);
        return new ResponseEntity<PaymentListResponse>(paymentListResponse, HttpStatus.OK);

    }
}
