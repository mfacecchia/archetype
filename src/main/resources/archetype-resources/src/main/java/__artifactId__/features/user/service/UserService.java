package ${package}.${artifactId}.features.user.service;

import org.springframework.stereotype.Service;

import ${package}.${artifactId}.common.exception.AlreadyRegisteredException;
import ${package}.${artifactId}.common.service.AbstractService;
import ${package}.${artifactId}.features.user.data.User;
import ${package}.${artifactId}.features.user.data.dto.request.UserCreateDto;
import ${package}.${artifactId}.features.user.data.dto.request.UserUpdateDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserPageDto;
import ${package}.${artifactId}.features.user.mapper.UserMapper;
import ${package}.${artifactId}.features.user.repository.UserRepository;

import jakarta.validation.Validator;

@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {
    private final UserRepository repository;

    public UserService(UserMapper userMapper, UserRepository userRepository, Validator validator) {
        super(userMapper, userRepository, validator, "User");
        this.repository = userRepository;
    }

    @Override
    protected void validateDelete(Integer id) {
    }

    @Override
    protected void validateUpdateDto(UserUpdateDto updateDto, User existing) {
    }

    @Override
    protected Integer getResourceId(User entity) {
        return entity.getId();
    }

    @Override
    protected void validateCreateDto(UserCreateDto createDto) {
        if (repository.existsByEmail(createDto.getEmail())) {
            throw new AlreadyRegisteredException("Email already exists");
        }

        if (repository.existsByExternalId(createDto.getExternalId())) {
            throw new AlreadyRegisteredException("ExternalId already exists");
        }
    }
}
