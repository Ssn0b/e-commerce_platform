package app.snob.ecommerce_platform.service.Impl;

import app.snob.ecommerce_platform.dto.UserResponse;
import app.snob.ecommerce_platform.entity.User;
import app.snob.ecommerce_platform.entity.enums.Role;
import app.snob.ecommerce_platform.repository.UserRepository;
import app.snob.ecommerce_platform.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers(String name, String role, int page, int size, String[] sort) {
        log.info("Executing getAllUsers method");
        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        List<User> users = userRepository.findAll(pageable).getContent();

        if (name != null) {
            users = users.stream()
                    .filter(product -> product.getName().contains(name))
                    .toList();
        }

        if (role != null) {
            users = users.stream()
                    .filter(product -> product.getRole().toString().contains(role))
                    .toList();
        }
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(UUID id) {
        log.info("Executing getUserById method with ID: {}", id);
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Executing getUserByEmail method with email: {}", email);
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        return user.map(this::mapToUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public void updateUsersRole(UUID id, String role) {
        log.info("Executing updateUsersRole method for user ID: {} with role: {}", id, role);
        if (role.isEmpty() || role.isBlank()) {
            log.error("IllegalArgumentException thrown at updateUsersRole method");
            throw new IllegalArgumentException("Role cannot be empty");
        }
        String roleUpperCase = role.toUpperCase();
        if (Arrays.stream(Role.values()).noneMatch(r -> r.name().equals(roleUpperCase))) {
            log.error("IllegalArgumentException thrown at updateUsersRole method");
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        user.setRole(Role.valueOf(role));

        userRepository.save(user);
    }

    @Override
    public void deleteUserById(UUID id) {
        log.info("Executing deleteUserById method for user ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            log.error("EntityNotFoundException thrown at deleteUserById method");
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }
}
