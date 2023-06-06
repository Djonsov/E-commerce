package ru.ecommerce.highstylewear.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ecommerce.highstylewear.dto.BucketDTO;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.Bucket;
import ru.ecommerce.highstylewear.repository.GenericRepository;

@Service
public class BucketService extends GenericService<Bucket, BucketDTO> {
    private final ItemService itemService;
    public BucketService(GenericRepository<Bucket> repository, GenericMapper<Bucket, BucketDTO> mapper, ItemService itemService) {
        super(repository, mapper);
        this.itemService = itemService;
    }

    public BucketDTO addItem(final Long itemId, Long bucketId){
        BucketDTO bucketDTO = getById(bucketId);
        bucketDTO.getItemsIds().add(itemService.getById(itemId));
        update(bucketDTO);
        return bucketDTO;
    }

    public BucketDTO clear(final Long bucketId) {
        BucketDTO bucketDTO = getById(bucketId);
        bucketDTO.getItemsIds().clear();
        repository.save(mapper.toEntity(bucketDTO));
        return bucketDTO;
    }

    public BucketDTO removeItem(final Long bucketId,final Long itemId) {
        BucketDTO bucketDTO = getById(bucketId);
        bucketDTO.getItemsIds().remove(itemService.getById(itemId));
        repository.save(mapper.toEntity(bucketDTO));
        return bucketDTO;
    }
}
