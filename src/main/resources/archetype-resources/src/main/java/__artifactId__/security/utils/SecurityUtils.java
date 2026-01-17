package ${package}.${artifactId}.security.utils;

import ${package}.${artifactId}.security.data.dto.response.JwtDto;
import ${package}.${artifactId}.common.exception.AuthenticationException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        throw new AuthenticationException();
    }

    public static String getCurrentJwtValue() {
        Jwt jwt = getCurrentJwt();
        return jwt.getTokenValue();
    }

    public static JwtDto getJwtDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtDto) authentication.getPrincipal();
    }
}
