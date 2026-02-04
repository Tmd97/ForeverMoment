package com.example.moment_forever.core.mapper;

import com.example.moment_forever.core.dto.auth.RegisterRequest;
import com.example.moment_forever.data.entities.ApplicationUser;
import com.example.moment_forever.data.entities.auth.AuthUser;

import java.time.LocalDateTime;
import java.util.Date;

public class AppUserBeanMapper {

    public static ApplicationUser mapDtoToEntity(RegisterRequest request, Long authUserId) {
        ApplicationUser applicationUser=new ApplicationUser();
        applicationUser.setAuthUserId(authUserId);
        applicationUser.setEmail(request.getEmail().toLowerCase().trim());
        applicationUser.setFullName(request.getFullName());
        applicationUser.setPhoneNumber(request.getPhoneNumber());
        applicationUser.setPreferredCity(request.getPreferredCity());
        applicationUser.setCreatedAt(LocalDateTime.now());
        return applicationUser;


    }

    public static void mapEntityToDto(AuthUser authUser) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(authUser.getUsername());
        registerRequest.setPassword(authUser.getPassword());

    }
}
