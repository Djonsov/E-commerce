package ru.ecommerce.highstylewear.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.model.Order;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.repository.UserRepository;
import ru.ecommerce.highstylewear.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderMapper extends GenericMapper<Order, OrderDTO> {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    protected OrderMapper(ModelMapper modelMapper, UserRepository userRepository, ItemRepository itemRepository) {
        super(Order.class, OrderDTO.class, modelMapper);
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(m -> m.skip(OrderDTO::setItemsIds))
                .addMappings(m-> m.skip(OrderDTO::setUser))
                .setPostConverter(toDTOConverter());

        modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setItems))
                .addMappings(m->m.skip(Order::setUser))
                .setPostConverter(toEntityConverter());

    }

    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        if(!Objects.isNull(source.getItemsIds())){
            destination.setItems(itemRepository.findAllById(source.getItemsIds()));
            destination.setUser(userRepository.findById(source.getUser()).orElseThrow(()->new NotFoundException("Пользователь не найден")));
        }else {
            destination.setItems(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setItemsIds(getIds(source));
        destination.setUser(source.getUser().getId());
    }

    @Override
    protected List<Long> getIds(Order entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getItems())
                ? null
                : entity.getItems().stream().map(GenericModel::getId).collect(Collectors.toList());
    }
}
