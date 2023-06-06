package ru.ecommerce.highstylewear.service;

import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface UserTestData {
    UserDTO USER_DTO_1 = new UserDTO().builder()
            .name("user1")
            .surName("test1")
            .birthDate(LocalDate.now())
            .orders(new ArrayList<>())
            .build();

    UserDTO USER_DTO_2 = new UserDTO().builder()
            .name("user2")
            .surName("test2")
            .birthDate(LocalDate.now())
            .orders(new ArrayList<>())
            .build();
    UserDTO USER_DTO_3 = new UserDTO().builder()
            .name("user3")
            .surName("test3")
            .birthDate(LocalDate.now())
            .orders(new ArrayList<>())
            .build();
    List<UserDTO> USER_DTO_LIST = Arrays.asList(USER_DTO_1,USER_DTO_2,USER_DTO_3);

    User USER_1 = new User(null,null,null,LocalDate.now(),"user1","user1",null,null,null,null,null,null,null);
    User USER_2 = new User(null,null,null,LocalDate.now(),"user2","user2",null,null,null,null,null,null,null);
    User USER_3 = new User(null,null,null,LocalDate.now(),"user3","user3",null,null,null,null,null,null,null);

    List<User> USER_LIST = Arrays.asList(USER_1,USER_2,USER_3);
}

