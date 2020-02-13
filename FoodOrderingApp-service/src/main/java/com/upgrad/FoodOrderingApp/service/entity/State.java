package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "state")
@NamedQueries({@NamedQuery(name = "stateByUuid", query = "select s from State s where s.uuid = :uuid"),
        @NamedQuery(name = "getStates", query = "select s from State s"),
        @NamedQuery(name = "stateById", query = "select s from State s where s.id = :id")

})
public class State {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name= "state_name")
    private String stateName;

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
