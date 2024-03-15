package app.snob.ecommerce_platform.controller;

import app.snob.ecommerce_platform.dto.ProductRequest;
import app.snob.ecommerce_platform.dto.ProductResponse;
import app.snob.ecommerce_platform.dto.UserResponse;
import app.snob.ecommerce_platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PatchMapping("/{id}")
    @Operation(summary = "Update User's Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's role updated successfully")
    })
    public ResponseEntity<String> updateRole(@PathVariable UUID id, @NotEmpty @RequestParam String role) {
        userService.updateUsersRole(id, role);
        return ResponseEntity.ok("User role updated successfully");
    }
    @GetMapping
    @Operation(summary = "Get Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    public List<UserResponse> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        return userService.getAllUsers(name, role, page, size, sort);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponse getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get User by Email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponse getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully")
    })
    public void deleteProduct(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }
}
