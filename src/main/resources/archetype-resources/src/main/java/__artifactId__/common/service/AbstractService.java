package ${package}.${artifactId}.common.service;

import ${package}.${artifactId}.common.data.dto.response.BasePageDto;
import ${package}.${artifactId}.common.data.entity.BaseAuditingEntity;
import ${package}.${artifactId}.common.exception.ResourceNotFoundException;
import ${package}.${artifactId}.common.exception.ValidationException;
import ${package}.${artifactId}.common.exception.errors.Error;
import ${package}.${artifactId}.common.exception.errors.ValidationError;
import ${package}.${artifactId}.common.exception.enums.InternalErrorCode;
import ${package}.${artifactId}.common.mapper.BaseMapper;
import ${package}.${artifactId}.common.repository.BaseRepository;
import ${package}.${artifactId}.common.specification.CommonSpecificationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractService<ENTITY extends BaseAuditingEntity, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE> {
    private final BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> mapper;
    private final BaseRepository<ENTITY, PK_TYPE> repository;
    protected final Validator validator;
    protected final String resourceName;

    protected abstract void validateCreateDto(CREATE_DTO createDto);

    protected abstract void validateUpdateDto(UPDATE_DTO updateDto, ENTITY existing);

    protected abstract void validateDelete(PK_TYPE id);

    protected abstract PK_TYPE getResourceId(ENTITY entity);

    public GET_DTO get(PK_TYPE id) {
        ENTITY entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        log.info("GetById ::: {} found with id {}", resourceName, id);

        return convertToDto(entity);
    }

    public PAGEABLE_DTO getAll(CommonSpecificationBuilder<ENTITY> specificationBuilder, Pageable pageable, boolean showTotalCount) {
        specificationBuilder.whereEqualTo("deleted", false, false);

        Specification<ENTITY> specification = specificationBuilder.build();

        List<ENTITY> entities;
        if (pageable != null) {
            entities = repository.findAll(specification, pageable.getSort());
        } else {
            entities = repository.findAll(specification);
        }

        entities = doFilter(entities);

        if (entities.isEmpty()) {
            log.info("GetAll ::: No entries were found with the given parameters. Returning an empty page.");
            return convertToPageDto(Page.empty());
        }

        if (pageable == null) {
            pageable = PageRequest.of(0, entities.size());
        }

        List<GET_DTO> entitiesSplit = getEntitiesPage(entities, pageable).stream()
                .map(this::convertToDto)
                .toList();

        Page<GET_DTO> page = new PageImpl<>(entitiesSplit, pageable, entities.size());

        PAGEABLE_DTO dto = convertToPageDto(page);

        if (showTotalCount) {
            dto.setTotalCount(page.getTotalElements());
        } else {
            dto.setTotalCount(null);
        }

        int pageNumber = page.getPageable().getPageNumber();
        log.info("GetAll ::: Found {} total results. Displaying first {} items on page {}", entities.size(), page.getNumberOfElements(), pageNumber);

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO create(CREATE_DTO createDto) {
        doValidate(createDto);
        validateCreateDto(createDto);
        ENTITY entity = convertToEntity(createDto);
        doCreate(entity);
        ENTITY saved = save(entity);

        log.info("Create ::: Created new {} with id", resourceName, getResourceId(saved));

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO update(PK_TYPE id, UPDATE_DTO updateDto) {
        ENTITY existing = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(resourceName, id.toString()));

        doValidate(updateDto);
        validateUpdateDto(updateDto, existing);

        convertUpdateDtoToEntity(updateDto, existing);
        doUpdate(existing, updateDto);
        ENTITY saved = save(existing);

        log.info("Update ::: Updated {} with id ", resourceName, id);

        return convertToDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(PK_TYPE id) {
        validateDelete(id);
        // Soft delete implemented by default. If you want to hard delete, override this
        // method in the service class
        softDelete(id);
    }

    protected void softDelete(PK_TYPE id) {
        Optional<ENTITY> existing = repository.findById(id);
        if (existing.isEmpty()) {
            log.info("Delete ::: {} with id {} was not found. Early returning.", resourceName, id);
            return;
        }

        ENTITY toDelete = existing.get();
        doDelete(toDelete);

        toDelete.setDeleted(true);
        save(toDelete);

        log.info("Delete ::: Execute soft delete on {} with id {}", resourceName, id);
    }

    protected void hardDelete(PK_TYPE id) {
        Optional<ENTITY> entity = repository.findById(id);

        if (!entity.isPresent()) {
            log.info("Delete ::: {} with id {} was not found. Early returning.", resourceName, id);
            return;
        }

        repository.delete(entity.get());
        log.info("Delete ::: Execute hard delete on {} with id {}", resourceName, id);
    }

    protected ENTITY save(ENTITY entity) {
        return repository.save(entity);
    }

    protected void doValidate(Object dto) {
        Set<ConstraintViolation<Object>> validationErrors = validator.validate(dto);
        List<Error> appValidationErrors = new ArrayList<>();

        for (ConstraintViolation<?> validationError : validationErrors) {
            String fieldName = validationError.getPropertyPath().iterator().next().getName();
            appValidationErrors.add(
                    new ValidationError(fieldName, InternalErrorCode.PARAMETER_INVALID, validationError.getMessage()));
        }
        if (appValidationErrors.size() >= 1) {
            throw new ValidationException(appValidationErrors);
        }
    }

    protected void doCreate(ENTITY toCreate) {
    }

    protected void doUpdate(ENTITY toUpdate, UPDATE_DTO updateDto) {
    }

    protected void doDelete(ENTITY entity) {
    }

    // Override this if you expect filtering based on
    // user roles or so
    protected List<ENTITY> doFilter(List<ENTITY> entities) {
        return entities;
    }

    public GET_DTO convertToDto(ENTITY entity) {
        return mapper.mapToDto(entity);
    }

    public ENTITY convertDtoToEntity(GET_DTO getDto) {
        return mapper.mapToEntity(getDto);
    }

    public PAGEABLE_DTO convertToPageDto(Page<GET_DTO> itemPage) {
        return mapper.mapPageToPageableDto(itemPage);
    }

    public ENTITY convertToEntity(CREATE_DTO createDto) {
        return mapper.mapCreateDtoToEntity(createDto);
    }

    public ENTITY convertUpdateDtoToEntity(UPDATE_DTO updateDto, ENTITY toUpdate) {
        mapper.mapUpdateDtoToEntity(updateDto, toUpdate);
        return toUpdate;
    }

    public UPDATE_DTO convertToUpdateDto(ENTITY entity) {
        return mapper.mapToUpdateDto(entity);
    }

    /**
     * Splits the provided list to the required
     * page, returning a portion of the same list.
     */
    private List<ENTITY> getEntitiesPage(List<ENTITY> entities, Pageable pageable) {
        if (pageable == null) {
            return entities;
        }

        if (entities.size() <= pageable.getOffset()) {
            return new ArrayList<>();
        }

        int start = (int) pageable.getOffset();

        int endIndex = start + pageable.getPageSize();
        int end = Math.min(endIndex, entities.size());

        return entities.subList(start, end);
    }
}
