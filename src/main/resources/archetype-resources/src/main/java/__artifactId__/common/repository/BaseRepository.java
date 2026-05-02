package ${package}.${artifactId}.common.repository;

import ${package}.${artifactId}.common.data.entity.BaseAuditingEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<ENTITY extends BaseAuditingEntity, PK_TYPE> extends JpaRepository<ENTITY, PK_TYPE>, JpaSpecificationExecutor<ENTITY> {

    Optional<ENTITY> findByIdAndDeleted(PK_TYPE id, Boolean deleted);
}
