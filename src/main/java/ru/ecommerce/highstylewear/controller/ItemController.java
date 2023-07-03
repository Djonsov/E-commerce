package ru.ecommerce.highstylewear.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.service.GenericService;
import ru.ecommerce.highstylewear.service.ItemService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("api/rest/items")
@Tag(name = "Предметы одежды", description = "Контроллер для работы с предметами одежды магазина")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "Bearer Authentication")
public class ItemController extends GenericController<Item, ItemDTO> {
    private final ItemService itemService;

    public ItemController(GenericService<Item, ItemDTO> genericService, ItemService itemService) {
        super(genericService);
        this.itemService = itemService;
    }


    @GetMapping("")
    public ResponseEntity<?> getAllNotDeleted(){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getAllNotDeleted());
    }

    @Override
    @PostMapping("/add")
    public ResponseEntity<ItemDTO> create(@RequestBody ItemDTO newEntity) {
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(newEntity));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestBody String title){

        return ResponseEntity.status(HttpStatus.OK).body(itemService.searchItems(title));
    }




}
