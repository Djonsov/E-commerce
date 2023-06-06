package ru.ecommerce.highstylewear.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.ecommerce.highstylewear.dto.BucketDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.DeleteException;
import ru.ecommerce.highstylewear.mapper.BucketMapper;
import ru.ecommerce.highstylewear.model.Bucket;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.BucketRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BucketServiceTest extends GenericTest<Bucket, BucketDTO> {
    private final ItemService itemService;

    public BucketServiceTest() {
        super();
        repository = Mockito.mock(BucketRepository.class);
        mapper = Mockito.mock(BucketMapper.class);
        itemService = Mockito.mock(ItemService.class);
        service = new BucketService(repository, mapper, itemService);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(BucketTestData.BUCKET_LIST);
        Mockito.when(mapper.toDTOs(BucketTestData.BUCKET_LIST)).thenReturn(BucketTestData.BUCKET_DTO_LIST);
        List<BucketDTO> bucketDTOS = service.getAll();
        log.info("Testing getAll(): " + bucketDTOS);
        assertEquals(BucketTestData.BUCKET_LIST.size(), bucketDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(BucketTestData.BUCKET_1));
        Mockito.when(mapper.toDTO(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_DTO_1);
        BucketDTO bucketDTO = service.getById(1L);
        log.info("Testing getById: " + bucketDTO);
        assertEquals(BucketTestData.BUCKET_DTO_1, bucketDTO);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(BucketTestData.BUCKET_DTO_1)).thenReturn(BucketTestData.BUCKET_1);
        Mockito.when(mapper.toDTO(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_DTO_1);
        Mockito.when(repository.save(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_1);
        BucketDTO bucketDTO = service.create(BucketTestData.BUCKET_DTO_1);
        log.info("Testing create(): " + bucketDTO);
        assertEquals(BucketTestData.BUCKET_DTO_1, bucketDTO);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(BucketTestData.BUCKET_DTO_1)).thenReturn(BucketTestData.BUCKET_1);
        Mockito.when(mapper.toDTO(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_DTO_1);
        Mockito.when(repository.save(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_1);
        BucketDTO bucketDTO = service.update(BucketTestData.BUCKET_DTO_1);
        log.info("Testing update(): " + bucketDTO);
        assertEquals(BucketTestData.BUCKET_DTO_1, bucketDTO);
    }

    @Test
    @Order(5)
    @Override
    protected void delete() throws DeleteException {
        Mockito.when(mapper.toEntity(BucketTestData.BUCKET_DTO_1)).thenReturn(BucketTestData.BUCKET_1);
        Mockito.when(mapper.toDTO(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_DTO_1);
        Mockito.when(repository.save(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(BucketTestData.BUCKET_1));
        log.info("Testing delete() before: " + BucketTestData.BUCKET_1.getIsDeleted());
        service.deleteSoft(1L);
        log.info("Testing delete() after: " + BucketTestData.BUCKET_1.getIsDeleted());
        assertTrue(BucketTestData.BUCKET_1.getIsDeleted());
    }

    @Test
    @Order(6)
    @Override
    protected void restore() {
        BucketTestData.BUCKET_1.setIsDeleted(true);
        Mockito.when(repository.save(BucketTestData.BUCKET_1)).thenReturn(BucketTestData.BUCKET_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(BucketTestData.BUCKET_1));
        log.info("Testing restore() before: " + BucketTestData.BUCKET_1.getIsDeleted());
        service.restore(1L);
        log.info("Testing restore() after: " + BucketTestData.BUCKET_1.getIsDeleted());
        assertFalse(BucketTestData.BUCKET_1.getIsDeleted());
    }

    @Test
    @Order(7)
    @Override
    protected void getAllNotDeleted() {
        BucketTestData.BUCKET_LIST.get(0).setIsDeleted(false);
        BucketTestData.BUCKET_LIST.get(1).setIsDeleted(false);
        BucketTestData.BUCKET_LIST.get(2).setIsDeleted(true);
        List<Bucket> buckets = BucketTestData.BUCKET_LIST.stream().filter(m -> !m.getIsDeleted()).collect(Collectors.toList());
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(buckets);
        Mockito.when(mapper.toDTOs(buckets)).thenReturn(BucketTestData.BUCKET_DTO_LIST.stream().limit(2).toList());
        List<BucketDTO> bucketDTOS = service.getAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + bucketDTOS);
        assertEquals(buckets.size(), bucketDTOS.size());

    }
}
