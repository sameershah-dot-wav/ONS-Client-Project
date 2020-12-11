package ons.group8.repositories;

import ons.group8.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepositoryJPA extends JpaRepository<User, Long> {

    Optional<User> findUsersByEmail(String email);

    boolean existsByEmail(String email);

    @Query("UPDATE User u SET u.failedAttempt = ?1 WHERE u.email = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String email);

    @Query(value = "INSERT INTO user_role(userId, roleId) VALUES(:userId, :roleId)", nativeQuery = true)
    void saveUserRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    User findUserByEmail(String email);

    User findUserById(Long userId);
}
