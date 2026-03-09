package com.example.groceries.groceries.item_to_buy.dtos;

import java.math.BigDecimal;

public record ItemCreateRequest(String name,
                                BigDecimal quantity,
                                String note,
                                Long storeId) {
}
