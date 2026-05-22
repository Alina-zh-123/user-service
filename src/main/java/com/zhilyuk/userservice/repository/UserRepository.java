package com.zhilyuk.userservice.repository;

import com.zhilyuk.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Modifying
    @Query(value = "UPDATE users SET active=true WHERE id=:id", nativeQuery = true)
    void activateUserById(@Param("id") Long id);
    @Modifying
    @Query(value = "UPDATE users SET active=false WHERE id=:id", nativeQuery = true)
    void deactivateUserById(@Param("id") Long id);
}
