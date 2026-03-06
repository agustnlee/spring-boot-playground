package com.agustin.backend.service;

import com.agustin.backend.dto.auth.AuthResponseDto;
import com.agustin.backend.dto.auth.LoginDto;
import com.agustin.backend.dto.auth.RegisterDto;
import com.agustin.backend.entity.Role;
import com.agustin.backend.entity.User;
import com.agustin.backend.exception.EmailAlreadyInUseException;
import com.agustin.backend.exception.InvalidInputException;
import com.agustin.backend.exception.UsernameAlreadyInUseException;
import com.agustin.backend.repository.UserRepository;
import com.agustin.backend.util.JwtHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtHelper jwtHelper) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper       = jwtHelper;
    }

    public AuthResponseDto register(RegisterDto dto) {
        // business logics
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new EmailAlreadyInUseException(dto.getEmail());

        if (userRepository.existsByUsername(dto.getUsername()))
            throw new UsernameAlreadyInUseException(dto.getUsername());

        User user = new User();
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // BCrypt for hashing
        user.setRole(Role.USER); // default, from client cannot define admin

        User saved = userRepository.save(user); // DB assigns id 

        String token = jwtHelper.generateToken(saved);

        return new AuthResponseDto(
            token,
            saved.getId(),
            saved.getEmail(),
            saved.getUsername(),
            saved.getRole().name()
        );
    }

    public AuthResponseDto login(LoginDto dto) {
        // find user by email
        User user = userRepository.findByEmail(dto.getEmail().toLowerCase().trim())
            .orElseThrow(() -> new InvalidInputException("Invalid email or password"));
            // vague on purpose

        // compare raw password against hash
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new InvalidInputException("Invalid email or password");

        String token = jwtHelper.generateToken(user);

        return new AuthResponseDto(
            token,
            user.getId(),
            user.getEmail(),
            user.getUsername(),
            user.getRole().name()
        );
    }
}