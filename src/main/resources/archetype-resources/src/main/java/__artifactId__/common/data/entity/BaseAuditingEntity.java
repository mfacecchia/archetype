package ${package}.${artifactId}.common.data.entity;

import ${package}.${artifactId}.security.utils.SecurityUtils;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@Audited
public class BaseAuditingEntity {
    @CreationTimestamp
    @Column(name = "created_at")
    protected Instant createdAt;

    @Column(name = "created_by")
    protected String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by")
    protected String updatedBy;

    @Column(name = "deleted")
    protected Boolean deleted;

    @PrePersist
    public void prePersist() {
        this.deleted = false;
        this.createdBy = SecurityUtils.getJwtUserId().toString();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedBy = SecurityUtils.getJwtUserId().toString();
    }
}
