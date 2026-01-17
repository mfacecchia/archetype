package ${package}.${artifactId}.common.data.dto.response;

import ${package}.${artifactId}.common.data.entity.BaseAuditingEntity;

import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseGetDto extends BaseAuditingEntity {
    protected Integer id;
}
