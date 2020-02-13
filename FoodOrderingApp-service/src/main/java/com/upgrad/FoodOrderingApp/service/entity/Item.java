package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item")
@NamedQueries({@NamedQuery(name = "findItemById", query = "select i from Item i where i.id = :id"),
        @NamedQuery(name = "findItemByUuid", query = "select i from Item i where i.uuid = :uuid")

 })
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name= "item_name")
    @NotNull
    private String itemName;

    @Column(name= "price")
    @NotNull
    private Integer price;

    @Column(name= "type")
    @NotNull
    private String type;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
