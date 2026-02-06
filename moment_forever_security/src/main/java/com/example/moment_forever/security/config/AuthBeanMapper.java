package com.example.moment_forever.security.config;
import com.example.moment_forever.data.entities.auth.AuthUser;

public class AuthBeanMapper {

    public static AuthUser mapDtoToEntity(RegisterRequest registerRequest) {
        AuthUser authUser= new AuthUser();
        authUser.setUsername(registerRequest.getEmail());
        authUser.setPassword(registerRequest.getPassword());
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        return authUser;

    }

     public static void mapEntityToDto(AuthUser authUser) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(authUser.getUsername());
        registerRequest.setPassword(authUser.getPassword());

    }
}
