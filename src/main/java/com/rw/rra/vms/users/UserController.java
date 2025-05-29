package com.rw.rra.vms.users;

import com.rw.rra.vms.users.DTO.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentLoggedInUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        // This would require adding a method to UserService to get user by ID
        // For now, we'll return the current user if the ID matches, otherwise return 404
        UserResponseDTO currentUser = userService.getCurrentLoggedInUser();
        if (currentUser.getId().equals(id)) {
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsersPaged(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByRole(
            @PathVariable Role role,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByRole(role, pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByStatus(
            @PathVariable UserStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(userService.getUsersByStatus(status, pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> searchUsersByName(
            @RequestParam String name,
            Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsersByName(name, pageable));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable UUID id, @RequestBody UserStatus status) {
        // This would require adding a method to UserService to update user status by ID
        // For now, we'll return 501 Not Implemented
        return ResponseEntity.status(501).body("Not implemented yet");
    }
}
