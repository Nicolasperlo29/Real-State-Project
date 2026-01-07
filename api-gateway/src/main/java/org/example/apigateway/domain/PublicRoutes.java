package org.example.apigateway.domain;

import java.util.List;

public class PublicRoutes {

    public static final List<String> PUBLIC_PATHS = List.of(
            "/properties/list",
            "/auth/login",
            "/auth/register",
            "/auth/refresh"
    );
}