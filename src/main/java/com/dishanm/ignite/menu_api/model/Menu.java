package com.dishanm.ignite.menu_api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(cascade=CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Item> items;

    @OneToMany(cascade=CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Modifier> modifiers;
}
