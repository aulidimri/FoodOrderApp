package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "coupon")
@NamedQueries({@NamedQuery(name = "couponByCouponName", query = "select c from Coupon c where c.couponName = :couponName"),
        @NamedQuery(name = "couponById", query = "select c from Coupon c where c.id = :id"),
        @NamedQuery(name = "couponByUuid", query = "select c from Coupon c where c.uuid = :uuid")

})
public class Coupon {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    private String couponName;

    @Column(name = "percent")
    @NotNull
    private int percent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
