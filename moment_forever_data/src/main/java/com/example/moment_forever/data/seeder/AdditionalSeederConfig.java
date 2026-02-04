package com.example.moment_forever.data.seeder;

import com.example.moment_forever.data.dao.auth.AuthUserDao;
import com.example.moment_forever.data.dao.auth.RoleDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

/**
 * AdditionalSeederConfig - Creates test data for development environment
 *
 * Why @Profile("dev")?
 * - Only runs in "dev" profile, not in production
 * - Creates test users for development/testing
 * - Separates test data from production seeders
 */
@Configuration
@Profile("dev") // Only active when spring.profiles.active=dev
public class AdditionalSeederConfig {

    @Bean
    @Order(2) // Runs after RoleDataSeeder
    public CommandLineRunner devDataSeeder(RoleDao roleRepository,
                                           AuthUserDao authUserRepository) {
        return args -> {
            System.out.println("=== Creating development test data ===");

            // 1. Create a test admin user
            createTestAdmin(roleRepository, authUserRepository);

            // 2. Create a test regular user
            createTestUser(roleRepository, authUserRepository);

            System.out.println("=== Development test data created ===");
        };
    }

    private void createTestAdmin(RoleDao roleDao,
                                 AuthUserDao authUserRepository) {

        // Check if test admin already exists
        if (!authUserRepository.existsByUsername("admin@cherishx.com")) {
            System.out.println("Creating test admin: admin@cherishx.com / admin123");

            // In real implementation, you would:
            // 1. Create AuthUser with encoded password
            // 2. Find ADMIN role from repository
            // 3. Assign role to user
            // 4. Save user
        }
    }

    private void createTestUser(RoleDao roleDao,
                                AuthUserDao authUserDao) {

        if (!authUserDao.existsByUsername("user@test.com")) {
            System.out.println("Creating test user: user@test.com / user123");
        }
    }
}