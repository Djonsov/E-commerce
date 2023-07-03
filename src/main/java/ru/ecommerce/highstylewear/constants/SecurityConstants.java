package ru.ecommerce.highstylewear.constants;

import java.util.List;

public interface SecurityConstants {
    List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/static/**",
            "/js/**",
            "/css/**",
            "/",
            "/swagger-ui/**",
            "/webjars/bootstrap/5.0.2/**",
            "/v3/api-docs/**",
            "/actuator/**");

    List<String> ITEMS_REST_WHITE_LIST = List.of("api/rest/items/**",
            "api/rest/items/{id}");

    List<String> BUCKETS_REST_WHITE_LIST = List.of("/api/rest/buckets/**");



    List<String> USERS_REST_WHITE_LIST = List.of("/users/auth",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password",
            "/users/change-password/user");

    List<String> ORDERS_REST_PERMISSION_LIST = List.of("/orders/add/**",
            "/orders/update/**",
            "/orders",
            "/orders/delete/**",
            "orders/order");

    List<String> BUCKETS_REST_PERMISSION_LIST = List.of("/buckets/**");

    List<String> ITEMS_REST_PERMISSION_LIST = List.of("api/rest/items/add/**",
            "api/rest/items/update/**",
            "api/rest/items/delete/**",
            "api/rest/items/restore/**");

    List<String> USERS_REST_PERMISSION_LIST = List.of("/users/getAll",
            "/users/create/**",
            "/users/update/**",
            "/users/delete/**",
            "users/add-stuff",
            "users/restore/**");
}

