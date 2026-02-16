package com.example.groceries.auth.user;

import com.example.groceries.auth.user.dtos.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse userToUserResponse(User user);
}
