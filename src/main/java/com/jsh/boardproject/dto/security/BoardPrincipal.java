package com.jsh.boardproject.dto.security;

import com.jsh.boardproject.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2Attributes

) implements UserDetails, OAuth2User {
    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        return BoardPrincipal.of(username,password,email,nickname,memo,Map.of());
    }

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo, Map<String, Object> oAuth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                email,
                nickname,
                memo,
                oAuth2Attributes
        );
    }

    public static BoardPrincipal from (UserAccountDto dto) {
         return BoardPrincipal.of(
                 dto.userId(),
                 dto.userPassword(),
                 dto.email(),
                 dto.nickname(),
                 dto.memo()
         );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.of(
                username,
                password,
                email,
                nickname,
                memo
        );
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; } // 권한, 권한과 인증은 다른개념

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override public Map<String, Object> getAttributes() { return oAuth2Attributes; }
    @Override public String getName() { return username; }

    @Getter
    @AllArgsConstructor
    public enum RoleType{
        USER("ROLE_USER");  // prefix 는 spring security 에서 권한 표현을 하는 규칙. 바꾸려면 옵션을 바꿔주면 된다.

        private final String name;
    }

}
