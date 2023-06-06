package ru.ecommerce.highstylewear.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ecommerce.highstylewear.model.User;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User> {
    User findUserByLogin(String login);
    User findUserByEmail(String email);

    User findUserByChangePasswordToken(String uuid);

    @Query(nativeQuery = true,
            value = """
                 select u.*
                 from users u
                 where u.name ilike '%' || coalesce(:name, '%') || '%'
                 and u.surname ilike '%' || coalesce(:surName, '%') || '%'
                 and u.login ilike '%' || coalesce(:login, '%') || '%'
                  """)
    List<User> searchUsers(String name,
                           String surName,
                           String login);
}
