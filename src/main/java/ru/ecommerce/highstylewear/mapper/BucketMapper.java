package ru.ecommerce.highstylewear.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.ecommerce.highstylewear.dto.BucketDTO;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.model.Bucket;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.service.ItemService;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BucketMapper extends GenericMapper<Bucket, BucketDTO> {
    //private final GenericRepository itemRepository;

    private final ItemRepository itemRepository;

    private final ItemService itemService;

    protected BucketMapper(ModelMapper modelMapper,
                           @Qualifier("itemRepository") ItemRepository itemRepository, ItemService itemService) {
        super(Bucket.class, BucketDTO.class, modelMapper);
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Bucket.class, BucketDTO.class)
                .addMappings(m -> m.skip(BucketDTO::setItemsIds)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(BucketDTO.class, Bucket.class)
                .addMappings(m -> m.skip(Bucket::setItems)).setPostConverter(toEntityConverter());
    }

    @Override
    protected void mapSpecificFields(BucketDTO source, Bucket destination) {
        if (!Objects.isNull(source.getItemsIds())) {
            List<Item> items = new ArrayList<>();
            for (ItemDTO itemDTO : source.getItemsIds()) {
                items.add(itemRepository.findItemById(itemDTO.getId()));
            }

            destination.setItems(items);
        } else {
            destination.setItems(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Bucket source, BucketDTO destination) {
        //destination.setItemsIds(getIds(source));
        List<ItemDTO> items = new ArrayList<>();

        for (Item item : source.getItems()) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setPrice(item.getPrice());
            itemDTO.setTitle(item.getTitle());
            itemDTO.setColor(item.getColor());
            itemDTO.setImage(item.getImage());
            itemDTO.setSize(item.getSize());
            itemDTO.setOrdersIds(null);
            items.add(itemDTO);
        }

        destination.setItemsIds(items);
    }

    @Override
    protected List<Long> getIds(Bucket entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getItems())
                ? null
                : entity.getItems().stream().map(GenericModel::getId).collect(Collectors.toList());
    }
}
