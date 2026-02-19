package com.example.groceries.groceries.item_to_buy;

import com.example.groceries.groceries.grocery_list.GroceryList;
import com.example.groceries.store.Store;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "item_seq", sequenceName = "seq_items_id", allocationSize = 1)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "quantity", nullable = false) //can be 0.5 for kg or liters
    private BigDecimal quantity;
    @Column(name = "note")
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grocery_list_id", nullable = false)
    private GroceryList groceryList;
    @ManyToMany(fetch = FetchType.LAZY)
//    @Builder.Default
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @JsonManagedReference
    @JoinTable(name = "items_stores",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id"))
    private List<Store> whereToBuy = new ArrayList<>();

}
