package com.example.groceries.auth.user;

import com.example.groceries.auth.user.dtos.UserCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById (Long id){
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }

    public User getUserByEmail (String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public User createUser (UserCreateRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new IllegalStateException("User with this email already exists");
        }
        User user = User.builder()
                .email(request.email())
                .name(request.name())
                .passwordHash(passwordEncoder.encode(request.password()))
                .build();
        return userRepository.save(user);
    }
}
