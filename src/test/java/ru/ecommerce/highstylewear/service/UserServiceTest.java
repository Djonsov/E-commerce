package ru.ecommerce.highstylewear.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.mockito.configuration.IMockitoConfiguration;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.DeleteException;
import ru.ecommerce.highstylewear.mapper.UserMapper;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest extends GenericTest<User, UserDTO> {
    public UserServiceTest() {
        super();
        repository = Mockito.mock(UserRepository.class);
        mapper = Mockito.mock(UserMapper.class);
        service = new UserService((UserRepository) repository, (UserMapper) mapper, null, null, null);
    }

    @Test
    @Order(1)
    @Override
    protected void getAll() {
        Mockito.when(repository.findAll()).thenReturn(UserTestData.USER_LIST);
        Mockito.when(mapper.toDTOs(UserTestData.USER_LIST)).thenReturn(UserTestData.USER_DTO_LIST);
        List<UserDTO> userDTOS = service.getAll();
        log.info("Testing getAll: " + userDTOS);
        assertEquals(UserTestData.USER_LIST.size(), userDTOS.size());
    }

    @Test
    @Order(2)
    @Override
    protected void getOne() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(UserTestData.USER_1));
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        UserDTO userDTO = service.getById(1L);
        log.info("Testing getById: " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);
    }

    @Test
    @Order(3)
    @Override
    protected void create() {
        Mockito.when(mapper.toEntity(UserTestData.USER_DTO_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        UserDTO userDTO = service.create(UserTestData.USER_DTO_1);
        log.info("Testing create(): " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);
    }

    @Test
    @Order(4)
    @Override
    protected void update() {
        Mockito.when(mapper.toEntity(UserTestData.USER_DTO_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        UserDTO userDTO = service.update(UserTestData.USER_DTO_1);
        log.info("Testing update(): " + userDTO);
        assertEquals(UserTestData.USER_DTO_1, userDTO);

    }

    @Test
    @Order(5)
    @Override
    protected void delete() throws DeleteException {
        Mockito.when(mapper.toEntity(UserTestData.USER_DTO_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(mapper.toDTO(UserTestData.USER_1)).thenReturn(UserTestData.USER_DTO_1);
        Mockito.when(repository.save(UserTestData.USER_1)).thenReturn(UserTestData.USER_1);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(UserTestData.USER_1));
        log.info("Testing delete() before: " + UserTestData.USER_1.getIsDeleted());
        service.deleteSoft(1L);
        log.info("Testing delete() after: " + UserTestData.USER_1.getIsDeleted());
        assertTrue(UserTestData.USER_1.getIsDeleted());
    }

    @Test
    @Order(6)
    @Override
    protected void restore() {
        UserTestData.USER_3.setIsDeleted(true);
        Mockito.when(repository.save(UserTestData.USER_3)).thenReturn(UserTestData.USER_3);
        Mockito.when(repository.findById(3L)).thenReturn(Optional.of(UserTestData.USER_3));
        log.info("Testing restore() before: " + UserTestData.USER_3.getIsDeleted());
        service.restore(3L);
        log.info("Testing restore() after: " + UserTestData.USER_3.getIsDeleted());
        assertFalse(UserTestData.USER_3.getIsDeleted());
    }

    @Test
    @Order(7)
    @Override
    protected void getAllNotDeleted() {
        UserTestData.USER_LIST.get(0).setIsDeleted(false);
        UserTestData.USER_LIST.get(1).setIsDeleted(false);
        UserTestData.USER_LIST.get(2).setIsDeleted(true);
        List<User> users = UserTestData.USER_LIST.stream().filter(m -> !m.getIsDeleted()).collect(Collectors.toList());
        Mockito.when(repository.findAllByIsDeletedFalse()).thenReturn(users);
        Mockito.when(mapper.toDTOs(users)).thenReturn(UserTestData.USER_DTO_LIST.stream().limit(2).toList());
        List<UserDTO> userDTOS = service.getAllNotDeleted();
        log.info("Testing getAllNotDeleted(): " + userDTOS);
        assertEquals(users.size(), userDTOS.size());
    }
}
