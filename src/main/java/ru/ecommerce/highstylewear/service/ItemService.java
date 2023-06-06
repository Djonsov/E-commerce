package ru.ecommerce.highstylewear.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.utils.FileHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService extends GenericService<Item, ItemDTO> {
    public ItemService(GenericRepository<Item> repository, GenericMapper<Item, ItemDTO> mapper) {
        super(repository, mapper);
    }

    public ItemDTO create(final ItemDTO newItem/*, MultipartFile file*/){
        //String fileName = FileHelper.createFile(file);
        //newItem.setImage(fileName);

        newItem.setCreatedWhen(LocalDateTime.now());
        newItem.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return mapper.toDTO(repository.save(mapper.toEntity(newItem)));
    }

    public List<ItemDTO> searchItems(String title){
        return mapper.toDTOs(((ItemRepository)repository).searchItemsByTitle(title));
    }
}
