package com.example.banktest.Repository;

import com.example.banktest.Entity.User;
import jakarta.persistence.Converter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String username);

    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUsername(@Param("userName") String username);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);
    //TODO Обработать ошибки
    @Query("SELECT u FROM User u WHERE " +
           "(:birthDate IS NULL OR u.birthDate > to_date(:birthDate, 'YYYY-MM-DD')) AND " +
           "(:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber) AND " +
           "(:firstName IS NULL OR u.firstName LIKE :firstName%) AND " +
           "(:middleName IS NULL OR u.middleName LIKE :middleName%) AND" +
           "(:lastName IS NULL OR u.lastName LIKE :lastName%) AND" +
           "(:email IS NULL OR u.email = :email)")
    List<User> findBySearchCriteria(@Param("birthDate") String birthDate,
                                    @Param("phoneNumber") String phoneNumber,
                                    @Param("firstName") String firstName,
                                    @Param("middleName") String middleName,
                                    @Param("lastName") String lastName,
                                    @Param("email") String email,
                                    Pageable pageable);

}
