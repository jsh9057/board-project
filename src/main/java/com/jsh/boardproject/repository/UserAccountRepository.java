package com.jsh.boardproject.repository;

import com.jsh.boardproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {
}
