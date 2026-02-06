package com.example.moment_forever.security.config;

import com.example.moment_forever.data.entities.ApplicationUser;

import java.time.LocalDateTime;

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

}
