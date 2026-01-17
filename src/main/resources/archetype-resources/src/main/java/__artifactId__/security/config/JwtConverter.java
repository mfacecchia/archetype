package ${package}.${artifactId}.security.config;

import ${package}.${artifactId}.security.data.AuthenticationToken;
import ${package}.${artifactId}.security.data.dto.response.JwtDto;
import ${package}.${artifactId}.security.mapper.JwtMapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtConverter implements Converter<Jwt, AuthenticationToken> {

    @Override
    public AuthenticationToken convert(Jwt jwt) {
        JwtDto jwtDto = JwtMapper.mapToDto(jwt);

        // TODO: Implement authenticated user roles obtaining logic

        // TODO: Replace `null` with the actual user roles
        // (or keep it as it is if you expect no roles)
        return new AuthenticationToken(jwt, jwtDto, null);
    }
}
