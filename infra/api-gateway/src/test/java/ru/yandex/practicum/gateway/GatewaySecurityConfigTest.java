package ru.yandex.practicum.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureWebTestClient
class GatewaySecurityConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserSecurityConfig config;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static String USER;
    private static String USER_PASS;
    private static String ADMIN;
    private static String ADMIN_PASS;

    private static final long PRODUCT_ID = 101;

    @BeforeEach
    void setUp() {
        for (UserSecurityConfig.UserConfig user : config.getUsers()) {
            if (user.getRoles().contains("ADMIN")) {
                ADMIN = user.getLogin();
                ADMIN_PASS = user.getPassword();
            } else {
                USER = user.getLogin();
                USER_PASS = user.getPassword();
            }
        }
    }

    @Test
    void catalogGet_isPublic() {
        webTestClient
                .get()
                .uri("/api/products")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void orderCreate_withoutCredentials_isUnauthorized() {
        webTestClient.post()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void orderCreate_withUserCredentials_passesSecurity() {
        webTestClient.post()
                .uri("/api/orders")
                .header(AUTHORIZATION_HEADER, basic(USER, USER_PASS))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void productWrite_withUserCredentials_isForbidden() {
        webTestClient.patch()
                .uri("/api/products/" + PRODUCT_ID)
                .header(AUTHORIZATION_HEADER, basic(USER, USER_PASS))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void productWrite_withAdminCredentials_passesSecurity() {
        webTestClient.patch()
                .uri("/api/products/" + PRODUCT_ID)
                .header(AUTHORIZATION_HEADER, basic(ADMIN, ADMIN_PASS))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getOrders_withUserCredentials_isForbidden() {
        webTestClient.get()
                .uri("/api/orders")
                .header(AUTHORIZATION_HEADER, basic(USER, USER_PASS))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getOrders_withAdminCredentials_passesSecurity() {
        webTestClient.get()
                .uri("/api/orders")
                .header(AUTHORIZATION_HEADER, basic(ADMIN, ADMIN_PASS))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void unknownRoute_withAdminCredentials_isForbidden() {
        webTestClient.get()
                .uri("/api/unknown")
                .header(AUTHORIZATION_HEADER, basic(ADMIN, ADMIN_PASS))
                .exchange()
                .expectStatus().isForbidden();

    }

    @Test
    void corsPreflight_isPublic() {
        webTestClient.options()
                .uri("/api/orders")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getOrder_withUserCredentials_passesSecurity() {
        webTestClient.get()
                .uri("/api/orders/1")
                .header(AUTHORIZATION_HEADER, basic(USER, USER_PASS))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getOrdersByCustomerEmail_withUserCredentials_passesSecurity() {
        webTestClient.get()
                .uri("/api/orders/by-email")
                .header(AUTHORIZATION_HEADER, basic(USER, USER_PASS))
                .exchange()
                .expectStatus().isOk();
    }

    private String basic(String username, String password) {
        String value = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    @TestConfiguration
    static class TestBackendConfig {

        @Bean
        RouterFunction<ServerResponse> testBackendRoutes() {
            return route(path("/api/**"), request -> ServerResponse.ok().build());
        }
    }
}