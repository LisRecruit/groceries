package com.example.groceries.store.dtos;

public record StoreCreateRequest(String name,
                                 Double latitude,
                                 Double longitude,
                                 String description) {
}
