package com.sharebook.demo.jwt;

import com.sharebook.demo.configuration.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter{
    //OncePerRequestFilter is a class that guarantees a single execution per request dispatch, on any servlet container.

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = resolveToken(request);//get the token from the request
        if (StringUtils.hasText(jwt) && !isUrlPermitted(request)) {
            Authentication authentication = jwtUtils.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isUrlPermitted(HttpServletRequest request) {
        String url = request.getRequestURI();
        if(url.equals("/authenticate") || url.equals("/users")) {
            return true;
        }
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        //get the token from the request
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
