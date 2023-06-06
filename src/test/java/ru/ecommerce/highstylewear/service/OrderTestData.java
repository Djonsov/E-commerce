package ru.ecommerce.highstylewear.service;

import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.model.Order;

import java.util.Arrays;
import java.util.List;

public interface OrderTestData {
    OrderDTO ORDER_DTO_1 = new OrderDTO().builder()
            .user(UserTestData.USER_1.getId())
            .details("test order 1")
            .itemsIds(List.of(ItemTestData.ITEM_1.getId()))
            .build();

    OrderDTO ORDER_DTO_2 = new OrderDTO().builder()
            .user(UserTestData.USER_2.getId())
            .details("test order 2")
            .itemsIds(List.of(ItemTestData.ITEM_2.getId()))
            .build();

    OrderDTO ORDER_DTO_3 = new OrderDTO().builder()
            .user(UserTestData.USER_3.getId())
            .details("test order 3")
            .itemsIds(List.of(ItemTestData.ITEM_3.getId(), ItemTestData.ITEM_2.getId()))
            .build();

    List<OrderDTO> ORDER_DTO_LIST = Arrays.asList(ORDER_DTO_1, ORDER_DTO_2, ORDER_DTO_3);

    Order ORDER_1 = new Order().builder()
            .details("test order 1")
            .items(List.of(ItemTestData.ITEM_1))
            .user(UserTestData.USER_1)
            .build();

    Order ORDER_2 = new Order().builder()
            .details("test order 2")
            .items(List.of(ItemTestData.ITEM_2))
            .user(UserTestData.USER_2)
            .build();

    Order ORDER_3 = new Order().builder()
            .details("test order 2")
            .items(List.of(ItemTestData.ITEM_1, ItemTestData.ITEM_2))
            .user(UserTestData.USER_3)
            .build();

    List<Order> ORDER_LIST = Arrays.asList(ORDER_1, ORDER_2, ORDER_3);
}
