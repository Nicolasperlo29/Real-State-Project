package org.example.usuarios.repository;

import org.example.usuarios.entity.AuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserEntity, Long> {

    Optional<AuthUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
