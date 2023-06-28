package com.jsh.boardproject.config;

import com.jsh.boardproject.domain.UserAccount;
import com.jsh.boardproject.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod
    public void securitySetUp(){
       given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
          "testId",
               "pw",
               "test@mail.com",
               "testNickname",
               "testMemo"
       )));
   }

}
