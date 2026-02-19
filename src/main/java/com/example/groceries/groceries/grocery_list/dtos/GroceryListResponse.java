package com.example.groceries.groceries.grocery_list.dtos;

import com.example.groceries.groceries.item_to_buy.dtos.ItemResponse;

import java.util.List;

public record GroceryListResponse(List<ItemResponse> items) {
}
