package com.samuelangan.mycompagny.authentification.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestTypeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Si l'URL commence par /api/, c'est une requête API REST
        if (uri.startsWith("/api/") || uri.startsWith("/management") || uri.startsWith("/swagger-ui")) {
            request.setAttribute("isApi", true);
        } else {
            request.setAttribute("isApi", false);
        }

        filterChain.doFilter(request, response);
    }
}

