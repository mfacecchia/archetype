package ${package}.${artifactId}.features.user.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "{validation.blank}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.blank}")
    private String firstName;

    private String middleName;

    @NotBlank(message = "{validation.blank}")
    private String lastName;

    @NotBlank(message = "{validation.blank}")
    private String externalId;
}
