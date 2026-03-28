package ${package}.${artifactId}.common.service;

import ${package}.${artifactId}.common.data.dto.response.BasePageDto;
import ${package}.${artifactId}.common.data.entity.BaseFileEntity;
import ${package}.${artifactId}.common.exception.ResourceNotFoundException;
import ${package}.${artifactId}.common.mapper.BaseMapper;
import ${package}.${artifactId}.common.repository.BaseRepository;

import jakarta.validation.Validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractFileService<ENTITY extends BaseFileEntity, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO extends BasePageDto<GET_DTO>, PK_TYPE>
        extends AbstractService<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO, PK_TYPE> {

    private final BaseRepository<ENTITY, PK_TYPE> repository;

    public AbstractFileService(BaseMapper<ENTITY, GET_DTO, CREATE_DTO, UPDATE_DTO, PAGEABLE_DTO> mapper,
            BaseRepository<ENTITY, PK_TYPE> repository, Validator validator, String resourceName) {

        super(mapper, repository, validator, resourceName);
        this.repository = repository;
    }

    public abstract String upload(MultipartFile file);

    public abstract InputStream getStreamByName(String filename);

    public abstract String delete(String filename);

    @SneakyThrows
    public InputStream getFile(PK_TYPE id) {
        ENTITY existing = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(resourceName, id.toString()));

        if(existing.getFilename() == null || existing.getFilename().isBlank()) {
            return new ByteArrayInputStream(new byte[0]);
        }

        InputStream fileStream = getStreamByName(existing.getFilename());

        log.info("GetFile ::: File found with name {} and id {}", existing.getFilename(), id);

        return fileStream;
    }

    @Transactional(rollbackFor = Exception.class)
    public GET_DTO create(CREATE_DTO createDto, MultipartFile file) {
        doValidate(createDto);
        validateCreateDto(createDto);

        ENTITY entity = convertToEntity(createDto);

        if (file != null && !file.isEmpty()) {
            doCreate(entity, file);
        } else {
            doCreate(entity);
        }

        ENTITY saved = save(entity);

        log.info("Create ::: File created and uploaded with name {} and id {}", saved.getFilename(), saved.getId());

        return convertToDto(saved);
    }

    @SneakyThrows
    protected void doCreate(ENTITY toCreate, MultipartFile file) {
        String filename = upload(file);
        toCreate.setFilename(filename);
    }

    @SneakyThrows
    @Override
    protected void doDelete(ENTITY toDelete) {
        delete(toDelete.getFilename());
    }
}
