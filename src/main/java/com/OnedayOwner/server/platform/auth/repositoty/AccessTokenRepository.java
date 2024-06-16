package com.OnedayOwner.server.platform.auth.repositoty;

import com.OnedayOwner.server.platform.auth.entity.AccessToken;
import com.OnedayOwner.server.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);

    List<AccessToken> findByUserAndToken(Optional<User> user, String token);

    Optional<AccessToken> findFirstByUserAndToken(User user, String token);
}
