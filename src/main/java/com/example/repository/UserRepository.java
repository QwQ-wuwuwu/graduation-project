package com.example.repository;

import com.example.pojo.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    @Query("select * from user u where u.role=:role")
    User findByRole(@Param("role") int role);
    @Query("select * from user u where u.number=:number and u.role=:role")
    User findByNumber(@Param("number") String number,@Param("role") int role);
    @Modifying
    @Query("update user set password=:password where number=:number")
    Integer updatePassword(@Param("number") String number, @Param("password") String password);
    @Modifying
    @Query("update user u set u.password=:password where u.id=:uid")
    Integer updatePasswordByUser(@Param("uid") String uid, @Param("password") String password);
}
