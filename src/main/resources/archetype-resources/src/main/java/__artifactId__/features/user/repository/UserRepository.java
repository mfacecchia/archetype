package ${package}.${artifactId}.features.user.repository;

import ${package}.${artifactId}.common.repository.BaseRepository;
import ${package}.${artifactId}.features.user.data.User;

public interface UserRepository extends BaseRepository<User, Integer> {

    boolean existsByEmail(String email);

    boolean existsByExternalId(String externalId);
}
