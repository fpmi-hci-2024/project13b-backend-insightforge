package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.users.EUserRole;
import com.bookstore.dev.domain.entities.users.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByTitle(EUserRole role);
}
