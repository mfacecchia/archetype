package ${package}.${artifactId}.features.user.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserField {
    FIRST_NAME("firstName"),
    MIDDLE_NAME("middleName"),
    LAST_NAME("lastName"),
    EMAIL("email");

    private final String path;
}

