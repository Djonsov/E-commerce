package ru.ecommerce.highstylewear.service;

import ru.ecommerce.highstylewear.dto.BucketDTO;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.model.Bucket;
import ru.ecommerce.highstylewear.model.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface BucketTestData {

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


    BucketDTO BUCKET_DTO_1 = new BucketDTO().builder()
            .itemsIds(Arrays.asList(ITEM_DTO_1))
            .build();

    BucketDTO BUCKET_DTO_2 = new BucketDTO().builder()
            .itemsIds(Arrays.asList(ITEM_DTO_2))
            .build();

    BucketDTO BUCKET_DTO_3 = new BucketDTO().builder()
            .itemsIds(Arrays.asList(ITEM_DTO_1, ITEM_DTO_2, ITEM_DTO_3))
            .build();

    List<BucketDTO> BUCKET_DTO_LIST = Arrays.asList(BUCKET_DTO_1, BUCKET_DTO_2, BUCKET_DTO_3);

    Bucket BUCKET_1 = new Bucket(Arrays.asList(ITEM_1));
    Bucket BUCKET_2 = new Bucket(Arrays.asList(ITEM_2));
    Bucket BUCKET_3 = new Bucket(Arrays.asList(ITEM_1, ITEM_2, ITEM_3));

    List<Bucket> BUCKET_LIST = Arrays.asList(BUCKET_1, BUCKET_2, BUCKET_3);
}
