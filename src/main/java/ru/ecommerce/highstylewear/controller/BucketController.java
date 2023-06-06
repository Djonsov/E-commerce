package ru.ecommerce.highstylewear.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.highstylewear.dto.BucketDTO;
import ru.ecommerce.highstylewear.model.Bucket;
import ru.ecommerce.highstylewear.service.BucketService;
import ru.ecommerce.highstylewear.service.GenericService;

@RestController
@RequestMapping("api/rest/buckets")
@Tag(name = "Корзина", description = "Контроллер для работы корзиной покупок")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BucketController extends GenericController<Bucket, BucketDTO>{
    private final BucketService bucketService;

    public BucketController(GenericService<Bucket, BucketDTO> genericService, BucketService bucketService) {
        super(genericService);
        this.bucketService = bucketService;
    }



    @PostMapping("/{bucketID}/addItem")
    public ResponseEntity<?> addItem( @PathVariable(value = "bucketID") Long bucketId,@RequestBody Long itemId){


        return ResponseEntity.status(HttpStatus.OK).body(((BucketService)service).addItem(itemId, bucketId));

    }

    @PostMapping("/{bucketId}/remove/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long bucketId, @PathVariable Long itemId){

        return ResponseEntity.status(HttpStatus.OK).body(bucketService.removeItem(bucketId,itemId));
    }

    @PostMapping("/{bucketID}/clear")
    public ResponseEntity<?> clear(@PathVariable(value = "bucketID") Long bucketId){

        return ResponseEntity.status(HttpStatus.OK).body(bucketService.clear(bucketId));
    }


}
