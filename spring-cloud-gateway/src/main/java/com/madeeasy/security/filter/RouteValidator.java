package com.madeeasy.security.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> OPEN_ENDPOINTS =
            List.of(
                    "/auth-service/authenticate"
            );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> OPEN_ENDPOINTS
                    .stream()
                    .noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}