package com.project.appvault.service;

import com.project.appvault.entity.User;
import com.project.appvault.exception.EmailAlreadyExistsException;
import com.project.appvault.exception.UsernameAlreadyExistsException;
import com.project.appvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("id"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void saveUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' is already taken");
        });
        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' is already taken");
        });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
