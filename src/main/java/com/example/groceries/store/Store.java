package com.example.groceries.store;

import com.example.groceries.groceries.item_to_buy.Item;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity (name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "store_seq", sequenceName = "seq_stores_id", allocationSize = 1)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "stores")
    private List<Item> items = new ArrayList<>();
}
