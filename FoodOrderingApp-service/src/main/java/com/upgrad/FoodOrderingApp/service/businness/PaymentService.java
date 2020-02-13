package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    public List<Payment> getPaymentMethods(){
        return paymentDao.getAllPaymentMethods();

    }

    public Payment getPaymentById(int id) {
        return paymentDao.getPaymentById(id);
    }

    public Payment getPaymentByUuid(String uuid) {
        return paymentDao.getPaymentByUuid(uuid);
    }
}
