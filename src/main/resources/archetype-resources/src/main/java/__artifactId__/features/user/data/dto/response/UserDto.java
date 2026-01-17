package ${package}.${artifactId}.features.user.data.dto.response;

import ${package}.${artifactId}.common.data.dto.response.BaseGetDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseGetDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}
