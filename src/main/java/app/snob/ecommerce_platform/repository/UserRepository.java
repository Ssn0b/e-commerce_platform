package app.snob.ecommerce_platform.repository;

import app.snob.ecommerce_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByConfirmationToken(UUID confirmationToken);
}
