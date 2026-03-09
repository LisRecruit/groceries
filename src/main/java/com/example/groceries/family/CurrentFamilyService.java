package com.example.groceries.family;

import com.example.groceries.auth.user.CustomUserDetails;
import com.example.groceries.auth.user.User;
import com.example.groceries.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CurrentFamilyService {

    private final UserService userService;

    public Family getFamilyFromUser(CustomUserDetails userDetails) {
        User user = userService.getUserById(userDetails.getUserId());

        if (user.getFamily() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User does not belong to any family"
            );
        }

        return user.getFamily();
    }
}
