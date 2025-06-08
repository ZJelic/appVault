package com.project.appvault.service;

import com.project.appvault.entity.User;
import com.project.appvault.exception.EmailAlreadyExistsException;
import com.project.appvault.exception.UserNotFoundException;
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
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
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
        userRepository.findByUsername(user.getUsername()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(user.getId())) {
                throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' is already taken");
            }
        });

        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(user.getId())) {
                throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' is already taken");
            }
        });

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + user.getId() + " not found"));
            user.setPassword(existingUser.getPassword());
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUsernameTakenByOther(String username, Long userId) {
        return userRepository.findByUsername(username)
                .filter(user -> !user.getId().equals(userId))
                .isPresent();
    }

    @Override
    public boolean isEmailTakenByOther(String email, Long userId) {
        return userRepository.findByEmail(email)
                .filter(user -> !user.getId().equals(userId))
                .isPresent();
    }
}
