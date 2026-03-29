package ${package}.${artifactId}.features.user.controller;

import ${package}.${artifactId}.common.data.dto.response.SuccessResponse;
import ${package}.${artifactId}.common.exception.data.dto.response.ErrorResponse;
import ${package}.${artifactId}.common.specification.CommonSpecificationBuilder;
import ${package}.${artifactId}.common.specification.PageableUtil;
import ${package}.${artifactId}.features.user.data.User;
import ${package}.${artifactId}.features.user.data.dto.request.UserCreateDto;
import ${package}.${artifactId}.features.user.data.dto.request.UserUpdateDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserPageDto;
import ${package}.${artifactId}.features.user.data.enums.UserField;
import ${package}.${artifactId}.features.user.service.UserService;
import ${package}.${artifactId}.features.user.specification.UserSpecificationBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/user")
@Tag(name = "Users", description = "Users CRUD operations")
public class UserController {
    private static final String ID_FIELD_NAME = "id";
    private static final String RESOURCE_NAME = "User";

    @Autowired
    UserService userService;

    @Operation(summary = "Get all " + RESOURCE_NAME + "s ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESOURCE_NAME + "s found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessResponse.class))}),

            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<UserPageDto>> getAll(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "middleName", required = false) String middleName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "showTotalCount", required = false, defaultValue = "false") Boolean showTotalCount) {

        Pageable pageable = PageableUtil.buildPageable(page, pageSize, sortBy, sortDirection);

        CommonSpecificationBuilder<User> specificationBuilder = new UserSpecificationBuilder()
                .like(UserField.FIRST_NAME.getPath(), firstName, false)
                .like(UserField.MIDDLE_NAME.getPath(), middleName, false)
                .like(UserField.LAST_NAME.getPath(), lastName, false)
                .like(UserField.EMAIL.name(), email, false);

        UserPageDto pageDto = userService.getAll(specificationBuilder, pageable, showTotalCount);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<UserPageDto> response = new SuccessResponse<>(responseStatus.value(), pageDto.getTotalCount() + " " + RESOURCE_NAME + "s found", pageDto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Get a " + RESOURCE_NAME + " by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the " + RESOURCE_NAME, content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessResponse.class))}),

            @ApiResponse(responseCode = "404", description = RESOURCE_NAME + " not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),

            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{" + ID_FIELD_NAME + "}")
    public ResponseEntity<SuccessResponse<UserDto>> get(
            @PathVariable(value = ID_FIELD_NAME) Integer id) {

        UserDto dto = userService.get(id);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<UserDto> response = new SuccessResponse<>(responseStatus.value(), RESOURCE_NAME + " was found", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Create a new " + RESOURCE_NAME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = RESOURCE_NAME + " was created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SuccessResponse.class))}),

            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<UserDto>> create(
            @Valid @RequestBody(required = true) UserCreateDto user) {

        UserDto dto = userService.create(user);

        HttpStatus responseStatus = HttpStatus.CREATED;
        SuccessResponse<UserDto> response = new SuccessResponse<>(responseStatus.value(), RESOURCE_NAME + " created successfully", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Update a " + RESOURCE_NAME + " by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESOURCE_NAME + " was updated", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))}),

            @ApiResponse(responseCode = "404", description = RESOURCE_NAME + " not found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),

            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PutMapping(path = "/{" + ID_FIELD_NAME + "}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse<UserDto>> update(
            @PathVariable(value = ID_FIELD_NAME) Integer id,
            @Valid @RequestBody(required = true) UserUpdateDto updateDto) {

        UserDto dto = userService.update(id, updateDto);

        HttpStatus responseStatus = HttpStatus.OK;
        SuccessResponse<UserDto> response = new SuccessResponse<>(responseStatus.value(), RESOURCE_NAME + " was updated", dto);

        return ResponseEntity.status(responseStatus).body(response);
    }

    @Operation(summary = "Delete a " + RESOURCE_NAME + " by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = RESOURCE_NAME + " was deleted"),

            @ApiResponse(responseCode = "500", description = "Generic server error", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{" + ID_FIELD_NAME + "}")
    public ResponseEntity<?> delete(
            @PathVariable(value = ID_FIELD_NAME) Integer id) {

        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

