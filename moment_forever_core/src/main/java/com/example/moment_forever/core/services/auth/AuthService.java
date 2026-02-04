package com.example.moment_forever.core.services.auth;

import com.example.moment_forever.core.dto.auth.AuthResponse;
import com.example.moment_forever.core.dto.auth.RegisterRequest;
import com.example.moment_forever.core.mapper.AppUserBeanMapper;
import com.example.moment_forever.core.mapper.AuthBeanMapper;
import com.example.moment_forever.data.dao.ApplicationUserDao;
import com.example.moment_forever.data.dao.auth.AuthUserDao;
import com.example.moment_forever.data.dao.auth.RoleDao;
import com.example.moment_forever.data.entities.ApplicationUser;
import com.example.moment_forever.data.entities.auth.AuthUser;
import com.example.moment_forever.data.entities.auth.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final AuthUserDao authUserDao;
    private final ApplicationUserDao applicationUserDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            AuthUserDao authUserDao,
            ApplicationUserDao applicationUserDao,
            RoleDao roleDao,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.authUserDao = authUserDao;
        this.applicationUserDao = applicationUserDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * 1. Register a new user
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        validateEmailNotExists(request.getEmail());
        // Step 1: Create and save AuthUser
        AuthUser authUser = createAuthUser(request);  // Transient
        AuthUser savedAuthUser = authUserDao.save(authUser);  // Now MANAGED

        // Step 2: Create and save ApplicationUser
        ApplicationUser appUser = createApplicationUser(request,savedAuthUser.getId());  // Transient
        ApplicationUser savedAppUser = applicationUserDao.save(appUser);  // Now MANAGED

        // Step 3: Link them together
        savedAuthUser.setExternalUserId(savedAppUser.getId());  // Auto-tracked
        savedAppUser.setAuthUserId(savedAuthUser.getId());      // Auto-tracked

        // Step 4: Assign role
        Role role = new Role("USER", "Default role for new users");
        roleDao.save(role);  // Persist role if not already present
        savedAuthUser.addRole(role);  // Auto-tracked (if role is managed)
        return buildRegistrationResponseWithoutToken(savedAuthUser,savedAppUser);
    }




    private AuthResponse buildRegistrationResponseWithoutToken(AuthUser authUser, ApplicationUser appUser) {
        //TODO using the JWT auth response for registration response , not good coding design, need to refactor later
        AuthResponse registrationResponse = new AuthResponse();
        registrationResponse.setEmail(authUser.getUsername());
        registrationResponse.setMessage("Registration successful for "+authUser.getUsername()+ " Please verify your email before logging in.");
        return registrationResponse;
    }

    private ApplicationUser createApplicationUser(RegisterRequest request, Long authUserId) {
        ApplicationUser applicationUser=AppUserBeanMapper.mapDtoToEntity(request, authUserId);
        try {
            return applicationUserDao.save(applicationUser);
        }
        catch (Exception e) {
            logger.error("Error creating ApplicationUser for authUserId: {}", authUserId, e);
            throw new RuntimeException("Internal server error during registration");
        }

    }

    private AuthUser createAuthUser(RegisterRequest request) {
        try {
            return AuthBeanMapper.mapDtoToEntity(request);
        }
        catch (Exception e) {
            logger.error("Error mapping RegisterRequest to AuthUser entity", e);
            throw new RuntimeException("Internal server error during registration");
        }
    }

    private void validateEmailNotExists(String email) {
        if (authUserDao.findByUsername(email).isPresent()) {
            logger.warn("Registration failed: Email already exists - {}", email);
            throw new IllegalArgumentException("Email already in use: " + email);
        }
    }

}
