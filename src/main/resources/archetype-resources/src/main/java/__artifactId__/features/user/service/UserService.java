package ${package}.${artifactId}.features.user.service;

import org.springframework.stereotype.Service;

import ${package}.${artifactId}.common.exception.AlreadyRegisteredException;
import ${package}.${artifactId}.common.exception.ConflictException;
import ${package}.${artifactId}.common.service.AbstractService;
import ${package}.${artifactId}.features.user.data.User;
import ${package}.${artifactId}.features.user.data.dto.request.UserCreateDto;
import ${package}.${artifactId}.features.user.data.dto.request.UserUpdateDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserDto;
import ${package}.${artifactId}.features.user.data.dto.response.UserPageDto;
import ${package}.${artifactId}.features.user.mapper.UserMapper;
import ${package}.${artifactId}.features.user.repository.UserRepository;

@Service
public class UserService extends AbstractService<User, UserDto, UserCreateDto, UserUpdateDto, UserPageDto, Integer> {

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.mapper = userMapper;
        this.repository = userRepository;
        this.resourceName = "User";
    }

    @Override
    protected void validateCreateDto(UserCreateDto createDto) {
        if (((UserRepository) repository).existsByEmail(createDto.getEmail())) {
            throw new ConflictException(resourceName, "Email", createDto.getEmail());
        }
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
}
