package ${package}.${artifactId}.features.user.data.dto.request;

import ${package}.${artifactId}.common.data.dto.request.BaseUpdateDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto extends BaseUpdateDto {
    @NotBlank(message = "{validation.blank}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.blank}")
    private String firstName;

    private String middleName;

    @NotBlank(message = "{validation.blank}")
    private String lastName;
}
