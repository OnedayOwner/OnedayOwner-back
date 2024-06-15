package com.OnedayOwner.server.platform.user.repository;

import com.OnedayOwner.server.platform.user.entity.Role;
import com.OnedayOwner.server.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByLoginIdAndRole(String loginId, Role role);

}
