package com.example.groceries.family.dtos.responses;

import com.example.groceries.auth.user.dtos.UserResponse;

import java.util.List;

public record FamilyResponse(String name,
                             int code,
                             UserResponse owner,
                             List<UserResponse> users) {
}
