package com.jsh.boardproject.config;

import com.jsh.boardproject.dto.UserAccountDto;
import com.jsh.boardproject.repository.UserAccountRepository;
import com.jsh.boardproject.service.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;
    @MockBean private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetUp(){
       given(userAccountService.searchUser(anyString()))
               .willReturn(Optional.of(creatUserAccountDto()));
       given(userAccountService.saveUser(anyString(),anyString(),anyString(),anyString(),anyString()))
               .willReturn(creatUserAccountDto());
   }

   private UserAccountDto creatUserAccountDto(){
        return UserAccountDto.of(
                "testId",
                "pw",
                "test@mail.com",
                "testNickname",
                "testMemo"
        );
   }
}
