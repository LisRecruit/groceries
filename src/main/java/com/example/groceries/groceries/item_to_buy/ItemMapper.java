package com.example.groceries.groceries.item_to_buy;

import com.example.groceries.groceries.item_to_buy.dtos.ItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemResponse toResponse (Item item);
}
