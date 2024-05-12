package org.example.test.config;

import org.example.test.service.AdminService;
import org.example.test.service.CustomerServiceRegister;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.core.Authentication;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerServiceRegister customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(adminService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public DaoAuthenticationProvider customerAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(customerService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(adminAuthenticationProvider())
                .authenticationProvider(customerAuthenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/favorite/remove/**").authenticated()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/customer/**", "/register**", "/home/js/**", "/home/css/**", "/home/img/**",
                                "/guest/**", "/product/detail/**", "/resources/**")
                        .permitAll()
                        .requestMatchers("/resources/**", "/profile/**", "/change-password").authenticated() // Ensure
                                                                                                             // /change-password
                                                                                                             // is
                                                                                                             // securely
                                                                                                             // accessible
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(loginSuccessHandler())
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .logoutSuccessHandler(customLogoutSuccessHandler())
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/profile/**"),
                                new AntPathRequestMatcher("/change-password"))); // Correct way to ignore CSRF for
                                                                                 // specific endpoints

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/index");
            }
        };
    }

    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException {
                if (authentication != null && authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    response.sendRedirect("/login");
                } else {
                    response.sendRedirect("/guest");
                }
            }
        };
    }
}
