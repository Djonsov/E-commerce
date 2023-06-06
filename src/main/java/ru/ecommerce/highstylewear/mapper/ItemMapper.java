package ru.ecommerce.highstylewear.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class ItemMapper extends GenericMapper<Item, ItemDTO>{
    private final OrderRepository orderRepository;

    protected ItemMapper( ModelMapper modelMapper, OrderRepository orderRepository) {
        super(Item.class, ItemDTO.class, modelMapper);
        this.orderRepository = orderRepository;
    }


    @Override
    protected void mapSpecificFields(ItemDTO source, Item destination) {
        if(!Objects.isNull(source.getOrdersIds())){
            destination.setOrders(orderRepository.findAllById(source.getOrdersIds()));
        }else {
            destination.setOrders(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Item source, ItemDTO destination) {
        destination.setOrdersIds(getIds(source));
    }

    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Item.class, ItemDTO.class)
                .addMappings(m->m.skip(ItemDTO::setOrdersIds)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(ItemDTO.class, Item.class)
                .addMappings(m->m.skip(Item::setOrders)).setPostConverter(toEntityConverter());


    }

    @Override
    protected List<Long> getIds(Item entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getOrders())
                ? null
                : entity.getOrders().stream().map(GenericModel::getId).collect(Collectors.toList());
    }
}
