package com.example.groceries.store;

import com.example.groceries.family.Family;
import com.example.groceries.groceries.item_to_buy.Item;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity (name = "stores")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "store_seq", sequenceName = "seq_stores_id", allocationSize = 1)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToMany(mappedBy = "stores")
    private List<Item> items = new ArrayList<>();
    @Embedded
    private StoreLocation location;
    private boolean verified;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_family_id", nullable = false)
    private Family createdByFamily;
}
