package ${package}.${artifactId}.common.data.entity;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@Audited
public class BaseEntityWithImage extends BaseEntity {
    @Column(name = "file_name", nullable = true)
    private String filename;
}
