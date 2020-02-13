package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "payment")
@NamedQueries({@NamedQuery(name = "getPaymentMethods", query = "select p from Payment p"),
        @NamedQuery(name = "getPaymentById", query = "select p from Payment p where p.id = :id"),
        @NamedQuery(name = "getPaymentByUuid", query = "select p from Payment p where p.uuid = :uuid")

})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name= "payment_name")
    private String paymentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
