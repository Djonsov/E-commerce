package ru.ecommerce.highstylewear.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.DeleteException;
import ru.ecommerce.highstylewear.mapper.ItemMapper;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.ItemRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTest extends GenericTest<Item, ItemDTO> {
    public ItemServiceTest() {
        super();
        repository = Mockito.mock(ItemRepository.class);
        mapper = Mockito.mock(ItemMapper.class);
        service = new ItemService(repository, mapper);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(ItemTestData.ITEM_LIST);
        Mockito.when(mapper.toDTOs(ItemTestData.ITEM_LIST)).thenReturn(ItemTestData.ITEM_DTO_LIST);
        List<ItemDTO> itemDTOS = service.getAll();
        log.info("Testing getAll(): " + itemDTOS);
        assertEquals(ItemTestData.ITEM_LIST.size(), itemDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(ItemTestData.ITEM_1));
        Mockito.when(mapper.toDTO(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_DTO_1);
        ItemDTO itemDTO = service.getById(1L);
        log.info("Testing getById(): " + itemDTO);
        assertEquals(ItemTestData.ITEM_DTO_1, itemDTO);

    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(ItemTestData.ITEM_DTO_1)).thenReturn(ItemTestData.ITEM_1);
        Mockito.when(mapper.toDTO(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_DTO_1);
        Mockito.when(repository.save(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_1);
        ItemDTO itemDTO = service.create(ItemTestData.ITEM_DTO_1);
        log.info("Testing create(): " + itemDTO);
        assertEquals(ItemTestData.ITEM_DTO_1, itemDTO);

    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(ItemTestData.ITEM_DTO_1)).thenReturn(ItemTestData.ITEM_1);
        Mockito.when(mapper.toDTO(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_DTO_1);
        Mockito.when(repository.save(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_1);
        ItemDTO itemDTO = service.update(ItemTestData.ITEM_DTO_1);
        log.info("Testing update(): " + itemDTO);
        assertEquals(ItemTestData.ITEM_DTO_1, itemDTO);

    }

    @Test
    @Order(5)
    @Override
    protected void delete() throws DeleteException {
        Mockito.when(mapper.toEntity(ItemTestData.ITEM_DTO_1)).thenReturn(ItemTestData.ITEM_1);
        Mockito.when(mapper.toDTO(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_DTO_1);
        Mockito.when(repository.save(ItemTestData.ITEM_1)).thenReturn(ItemTestData.ITEM_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(ItemTestData.ITEM_1));
        log.info("Testing delete() before: " + ItemTestData.ITEM_1.getIsDeleted());
        service.deleteSoft(1L);
        log.info("Testing delete() after: " + ItemTestData.ITEM_1.getIsDeleted());
        assertTrue(ItemTestData.ITEM_1.getIsDeleted());
    }

    @Test
    @Order(6)
    @Override
    protected void restore() {
        ItemTestData.ITEM_3.setIsDeleted(true);
        Mockito.when(repository.save(ItemTestData.ITEM_3)).thenReturn(ItemTestData.ITEM_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(ItemTestData.ITEM_3));
        log.info("Testing restore() before: " + ItemTestData.ITEM_3.getIsDeleted());
        service.restore(3L);
        log.info("Testing restore() after: " + ItemTestData.ITEM_3.getIsDeleted());
        assertFalse(ItemTestData.ITEM_3.getIsDeleted());
    }

    @Test
    @Order(7)
    @Override
    protected void getAllNotDeleted() {
        ItemTestData.ITEM_LIST.get(0).setIsDeleted(false);
        ItemTestData.ITEM_LIST.get(1).setIsDeleted(false);
        ItemTestData.ITEM_LIST.get(2).setIsDeleted(true);
        List<Item> items = ItemTestData.ITEM_LIST.stream().filter(m->!m.getIsDeleted()).collect(Collectors.toList());
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(items);
        Mockito.when(mapper.toDTOs(items)).thenReturn(ItemTestData.ITEM_DTO_LIST.stream().limit(2).toList());
        List<ItemDTO> itemDTOS = service.getAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + itemDTOS);
        assertEquals(items.size(),itemDTOS.size());

    }
}
