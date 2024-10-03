package com.cs2404.tablebuddy.jh.service;

import com.cs2404.tablebuddy.jh.entity.User;
import com.cs2404.tablebuddy.jh.dto.SignUpRequest;
import com.cs2404.tablebuddy.jh.dto.SignUpResponse;
import com.cs2404.tablebuddy.jh.repository.UserRepository;
import com.cs2404.tablebuddy.jh.util.UserUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {
        validateEmail(request.getEmail());

        User user = UserUtility.convertToUser(request);
        User createdUser = userRepository.save(user);

        return UserUtility.converToSignUpResponse(createdUser);
    }

    public void validateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Duplicate email: " + email);
                });
    }
}
