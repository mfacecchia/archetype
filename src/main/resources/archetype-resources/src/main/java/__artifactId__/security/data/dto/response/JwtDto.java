package ${package}.${artifactId}.security.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {
    // TODO: Configure with your own jwt claims
    private String userId;
    private String externalId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String email;
}
