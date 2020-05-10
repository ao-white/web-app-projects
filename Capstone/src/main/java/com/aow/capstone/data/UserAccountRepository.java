package com.aow.capstone.data;

import com.aow.capstone.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alex White
 */
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    UserAccount findByUserName(String name);
}
