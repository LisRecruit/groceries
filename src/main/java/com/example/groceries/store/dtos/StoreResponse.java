package com.example.groceries.store.dtos;

import lombok.Builder;
import lombok.Setter;

@Builder
@Setter
public record StoreResponse(Long id,
                            String name,
                            Double latitude,
                            Double longitude,
                            String description) {
}
