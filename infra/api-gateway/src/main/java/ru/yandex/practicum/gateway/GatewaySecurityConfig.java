package ru.yandex.practicum.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

    private final UserSecurityConfig userSecurityConfig;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.GET,
                                "/api/categories/**",
                                "/api/products/**",
                                "/api/inventory/**")
                        .permitAll()
                        .pathMatchers(HttpMethod.GET,"/api/orders").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/orders/**").hasRole("USER")
                        .pathMatchers(HttpMethod.GET, "/api/orders/**").hasRole("USER")
                        .pathMatchers("/api/products/**").hasRole("ADMIN")
                        .pathMatchers("/api/categories/**").hasRole("ADMIN")
                        .pathMatchers("/api/inventory/**").hasRole("ADMIN")
                        .anyExchange().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails[] users = userSecurityConfig.getUsers().stream()
                .map(config -> toUserDetails(config, passwordEncoder))
                .toArray(UserDetails[]::new);

        return new MapReactiveUserDetailsService(users);
    }

    private UserDetails toUserDetails(UserSecurityConfig.UserConfig config, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(config.getPassword());

        return User.builder()
                .username(config.getLogin())
                .password(encodedPassword)
                .roles(config.getRoles().toArray(String[]::new))
                .build();
    }
}
