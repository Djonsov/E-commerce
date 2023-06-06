package ru.ecommerce.highstylewear.service;

import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface ItemTestData {
    ItemDTO ITEM_DTO_1 = new ItemDTO().builder()
            .title("t-shirt")
            .price(500D)
            .size(44)
            .image("1")
            .color("black")
            .ordersIds(new ArrayList<>())
            .build();

    ItemDTO ITEM_DTO_2 = new ItemDTO().builder()
            .title("t-shirt")
            .price(1500D)
            .size(48)
            .image("2")
            .color("white")
            .ordersIds(new ArrayList<>())
            .build();

    ItemDTO ITEM_DTO_3 = new ItemDTO().builder()
            .title("shirt")
            .price(1000D)
            .size(46)
            .image("3")
            .color("blue")
            .ordersIds(new ArrayList<>())
            .build();

    List<ItemDTO> ITEM_DTO_LIST = Arrays.asList(ITEM_DTO_1, ITEM_DTO_2, ITEM_DTO_3);

    Item ITEM_1 = new Item("t-shirt", 500D, "black", 44, "1", null, null);
    Item ITEM_2 = new Item("t-shirt", 1500D, "white", 48, "2", null, null);
    Item ITEM_3 = new Item("shirt", 1000D, "blue", 46, "3", null, null);

    List<Item> ITEM_LIST = Arrays.asList(ITEM_1, ITEM_2, ITEM_3);
}
