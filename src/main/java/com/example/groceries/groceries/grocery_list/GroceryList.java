package com.example.groceries.groceries.grocery_list;

import com.example.groceries.family.Family;
import com.example.groceries.groceries.item_to_buy.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "grocery_lists")
public class GroceryList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "grocery_list_seq", sequenceName = "seq_grocery_lists_id", allocationSize = 1)
    private Long id;
    @OneToMany(mappedBy = "groceryList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
    @OneToOne(mappedBy = "groceryList", optional = false)
    private Family family;
    public void addItem(Item item) {
        items.add(item);
        item.setGroceryList(this);
    }
    public void removeItem(Item item) {
        items.remove(item);
        item.setGroceryList(null);
    }

}
