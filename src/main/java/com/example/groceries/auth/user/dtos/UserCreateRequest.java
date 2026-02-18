package com.example.groceries.auth.user.dtos;

public record UserCreateRequest(String email, String password, String name) {
}
