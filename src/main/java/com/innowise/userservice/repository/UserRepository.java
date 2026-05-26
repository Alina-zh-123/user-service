package com.innowise.userservice.repository;

import com.innowise.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Modifying
    @Query(value = "UPDATE users SET active:=activate WHERE id=:id", nativeQuery = true)
    void activateUserById(@Param("id") Long id, @Param("activate") Boolean activate);

    @Query("SELECT user FROM User user LEFT JOIN FETCH user.cards WHERE user.id = :id")
    Optional<User> findByIdWithCards(@Param("id") Long id);
}
