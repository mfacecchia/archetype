package ${package}.${artifactId}.security.mapper;

import ${package}.${artifactId}.security.data.dto.response.JwtDto;

import org.springframework.security.oauth2.jwt.Jwt;

public class JwtMapper {

    public static JwtDto mapToDto(Jwt jwt) {
        JwtDto dto = new JwtDto();

        // TODO: Update with your own jwt claims
        dto.setUserId(jwt.getClaimAsString("user_id"));
        dto.setExternalId(jwt.getClaimAsString("sub"));
        dto.setFirstName(jwt.getClaimAsString("given_name"));
        dto.setMiddleName(jwt.getClaimAsString("middle_name"));
        dto.setLastName(jwt.getClaimAsString("family_name"));
        dto.setFullName(jwt.getClaimAsString("name"));
        dto.setEmail(jwt.getClaimAsString("email"));

        return dto;
    }
}
