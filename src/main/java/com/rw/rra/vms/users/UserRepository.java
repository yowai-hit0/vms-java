package com.rw.rra.vms.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailOrMobile(String email, String mobile);
    Optional<User> findByEmail(String email);

    @Override
    Optional<User> findById(UUID userId);

    Page<User> findAll(Pageable pageable);
    Page<User> findByRole(Role role, Pageable pageable);
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    Page<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName, Pageable pageable);
}
