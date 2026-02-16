package com.example.groceries.family;

import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserMapper;
import com.example.groceries.auth.user.dtos.UserResponse;
import com.example.groceries.family.dtos.responses.FamilyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring", uses = {UserMapper.class})
public interface FamilyMapper {

    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "users", target = "users")
    FamilyResponse familyToFamilyResponse(Family family);
}
