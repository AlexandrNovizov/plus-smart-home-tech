package ru.yandex.practicum.gateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security")
@Getter
@Setter
public class UserSecurityConfig {

    public List<UserConfig> users;

    @Getter
    @Setter
    public static class UserConfig {
        private String login;
        private String password;
        private List<String> roles;
    }
}
