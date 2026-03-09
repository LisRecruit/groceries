package com.example.groceries.groceries.item_to_buy.dtos;

import java.util.List;

public record DeleteSetOfItemsRequest(List<ItemRequest> items) {
}
