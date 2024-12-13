package com.bookstore.dev.domain.repositories;

import com.bookstore.dev.domain.entities.users.User;
import com.bookstore.dev.domain.entities.users.UserToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepository extends CrudRepository<UserToken, Long> {
    Optional<UserToken> findByAccess(String access);

    Optional<UserToken> findByRefresh(String refresh);

    Optional<UserToken> findByRefreshAndUser(String refresh, User user);

    List<UserToken> findByUser(User user);
}
