package com.example.groceries.groceries.item_to_buy.dtos;

import java.util.List;

public record CreateSetOfItemsRequest(List<ItemCreateRequest> items) {
}
