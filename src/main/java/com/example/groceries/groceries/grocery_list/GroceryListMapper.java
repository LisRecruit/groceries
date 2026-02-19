package com.example.groceries.groceries.grocery_list;


import com.example.groceries.groceries.grocery_list.dtos.GroceryListResponse;
import com.example.groceries.groceries.item_to_buy.Item;
import com.example.groceries.groceries.item_to_buy.ItemMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface GroceryListMapper {
    GroceryListResponse itemListToResponse(List<Item>items);
    GroceryListResponse toResponse(GroceryList groceryList);
}
