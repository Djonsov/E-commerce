package ru.ecommerce.highstylewear.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.exception.DeleteException;
import ru.ecommerce.highstylewear.mapper.OrderMapper;
import ru.ecommerce.highstylewear.model.Order;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.repository.OrderRepository;
import ru.ecommerce.highstylewear.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest extends GenericTest<Order, OrderDTO> {
    public OrderServiceTest() {
        repository = Mockito.mock(OrderRepository.class);
        mapper = Mockito.mock(OrderMapper.class);
        UserService userService = Mockito.mock(UserService.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
        ItemService itemService = Mockito.mock(ItemService.class);
        ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
        service = new OrderService(repository,mapper, itemService, null, userService,(OrderRepository) repository,userRepository,javaMailSender,itemRepository);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(OrderTestData.ORDER_LIST);
        Mockito.when(mapper.toDTOs(OrderTestData.ORDER_LIST)).thenReturn(OrderTestData.ORDER_DTO_LIST);
        List<OrderDTO> orderDTOS = service.getAll();
        log.info("Testing getAll: " + orderDTOS);
        assertEquals(OrderTestData.ORDER_LIST.size(),orderDTOS.size());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(OrderTestData.ORDER_1));
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        OrderDTO orderDTO = service.getById(1L);
        log.info("Testing getById: " + orderDTO);
        assertEquals(OrderTestData.ORDER_DTO_1,orderDTO);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(OrderTestData.ORDER_DTO_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        OrderDTO orderDTO = service.create(OrderTestData.ORDER_DTO_1);
        log.info("Testing create(): " + orderDTO);
        assertEquals(OrderTestData.ORDER_DTO_1, orderDTO);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(OrderTestData.ORDER_DTO_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        OrderDTO orderDTO = service.update(OrderTestData.ORDER_DTO_1);
        log.info("Testing update(): " + orderDTO);
        assertEquals(OrderTestData.ORDER_DTO_1, orderDTO);

    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @Override
    protected void delete() throws DeleteException {
        Mockito.when(mapper.toEntity(OrderTestData.ORDER_DTO_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(mapper.toDTO(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_DTO_1);
        Mockito.when(repository.save(OrderTestData.ORDER_1)).thenReturn(OrderTestData.ORDER_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(OrderTestData.ORDER_1));
        log.info("Testing delete() before: " + OrderTestData.ORDER_1.getIsDeleted());
        service.deleteSoft(1L);
        log.info("Testing delete() after: " + OrderTestData.ORDER_1.getIsDeleted());
        assertTrue(OrderTestData.ORDER_1.getIsDeleted());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @Override
    protected void restore() {
        OrderTestData.ORDER_3.setIsDeleted(true);
        Mockito.when(repository.save(OrderTestData.ORDER_3)).thenReturn(OrderTestData.ORDER_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(OrderTestData.ORDER_3));
        log.info("Testing restore() before: " + OrderTestData.ORDER_3.getIsDeleted());
        service.restore(3L);
        log.info("Testing restore() after: " + OrderTestData.ORDER_3.getIsDeleted());
        assertFalse(OrderTestData.ORDER_3.getIsDeleted());
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @Override
    protected void getAllNotDeleted() {
        OrderTestData.ORDER_LIST.get(0).setIsDeleted(false);
        OrderTestData.ORDER_LIST.get(1).setIsDeleted(false);
        OrderTestData.ORDER_LIST.get(2).setIsDeleted(true);
        List<Order> orders = OrderTestData.ORDER_LIST.stream().filter(m->!m.getIsDeleted()).collect(Collectors.toList());
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(orders);
        Mockito.when(mapper.toDTOs(orders)).thenReturn(OrderTestData.ORDER_DTO_LIST.stream().limit(2).toList());
        List<OrderDTO> orderDTOS = service.getAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + orderDTOS);
        assertEquals(orders.size(),orderDTOS.size());
    }
}
