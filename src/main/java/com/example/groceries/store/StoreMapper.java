package com.example.groceries.store;

import com.example.groceries.store.dtos.StoreResponse;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface StoreMapper {
    StoreResponse toResponse (Store store);
}
